package newsfeeds.services;

import com.mongodb.WriteResult;
import newsfeeds.Utils.Utils;
import newsfeeds.helpers.MongoHelper;
import newsfeeds.models.CategoryDataModel;
import newsfeeds.models.NewsFeedDataModel;
import newsfeeds.wrappers.ResponseWrapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by samarth on 17/11/14.
 */
public class CategoryServiceImpl implements CategoryService {

    /**
     * @param jsonData
     * @return
     */
    @Override
    public ResponseWrapper createCategory(String jsonData) {
        CategoryDataModel categoryData;
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try {
            categoryData = Utils.getObjectMapper().readValue(jsonData, CategoryDataModel.class);
            MongoHelper.save(categoryData);
            responseWrapper.setResult(categoryData);
            responseWrapper.setSuccess(true);
            String statusMessage = String.format("Successfully created '%s' category", categoryData.getName());
            responseWrapper.setStatus(statusMessage);
        }
        catch (Exception e) {
            responseWrapper.setResult(null);
            responseWrapper.setSuccess(false);
            responseWrapper.setStatus(e.getMessage());
        }
        return responseWrapper;
    }

    /**
     * @param jsonData
     * @param category_id
     * @return
     */
    @Override
    public ResponseWrapper addNewsFeedToCategory(String jsonData, String category_id) {
        NewsFeedDataModel newsFeed;
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try {
            newsFeed = Utils.getObjectMapper().readValue(jsonData, NewsFeedDataModel.class);
            newsFeed.setCategoryId(category_id);
            MongoHelper.save(newsFeed);
            String news_id = newsFeed.getId();
            // Update Category collection where category_id is
            Update update = new Update();
            update.addToSet("newsListId", news_id);
            updateCategoryDetailsById(category_id, update);

            responseWrapper.setResult(newsFeed);
            responseWrapper.setSuccess(true);
            String statusMessage = String.format("Successfully created '%s' news feed", newsFeed.getName());
            responseWrapper.setStatus(statusMessage);
        }
        catch (Exception e) {
            responseWrapper.setResult(null);
            responseWrapper.setSuccess(false);
            responseWrapper.setStatus(e.getMessage());
        }

        return responseWrapper;

    }

    /**
     * @param category_id
     * @param update
     * @return
     */
    @Override
    public ResponseWrapper updateCategoryDetailsById(String category_id, Update update) {
        WriteResult wr = MongoHelper.update(Query.query(Criteria.where("_id").is(category_id)),
                update.set("updated_at", new Date()),
                CategoryDataModel.class);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setResult(wr);
        responseWrapper.setStatus("Updated Category Details");
        return responseWrapper;
    }

    /**
     * @param news_id
     * @param update
     * @return
     */
    @Override
    public ResponseWrapper updateNewsFeedDetailsById(String news_id, Update update) {
        WriteResult wr = MongoHelper.update(Query.query(Criteria.where("_id").is(news_id)),
                update.set("updated_at", new Date()),
                NewsFeedDataModel.class);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setResult(wr);
        responseWrapper.setStatus("Updated News Feed Details");
        return responseWrapper;
    }

    /**
     * @param category_id
     * @return
     */
    @Override
    public ResponseWrapper deleteCategoryById(String category_id) {
        Update update = new Update();
        update.set("is_deleted", true);
        ResponseWrapper responseWrapper = updateCategoryDetailsById(category_id, update);
        responseWrapper.setStatus(String.format("Deleted Category with id '%s'", category_id));
        return responseWrapper;
    }

    /**
     * @param news_id
     * @return
     */
    @Override
    public ResponseWrapper deleteNewsById(String news_id) {
        Update update = new Update();
        update.set("is_deleted", true);
        ResponseWrapper responseWrapper = updateNewsFeedDetailsById(news_id, update);
        responseWrapper.setStatus(String.format("Deleted News with id '%s'", news_id));
        return responseWrapper;
    }

    /**
     * @param news_id
     * @return
     */
    @Override
    public ResponseWrapper unpublishNewsById(String news_id) {
        Update update = new Update();
        update.set("is_published", false);
        ResponseWrapper responseWrapper = updateNewsFeedDetailsById(news_id, update);
        responseWrapper.setStatus(String.format("Unpublished Category with id '%s'", news_id));
        return responseWrapper;
    }

    /**
     * @param category_id
     * @return
     */
    @Override
    public ResponseWrapper unpublishCategoryById(String category_id) {
        Update update = new Update();
        update.set("is_published", false);
        ResponseWrapper responseWrapper = updateCategoryDetailsById(category_id, update);
        responseWrapper.setStatus(String.format("Unpublished Category with id '%s'", category_id));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper getPublishedData() {
        List<CategoryDataModel> categoryDocuments = MongoHelper.find(Query.query(Criteria.where("is_deleted").ne(true).and("is_published").is(true)), CategoryDataModel.class);
        for(CategoryDataModel categoryData : categoryDocuments) {
            List<NewsFeedDataModel> newsList = MongoHelper
                    .find(Query.query(Criteria.where("_id").in(categoryData.getNewsListId()).and("is_deleted").ne(true).and("is_published").is(true)),
                            NewsFeedDataModel.class);
            categoryData.setNewsList(newsList);
        }
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setResult(categoryDocuments);
        responseWrapper.setSuccess(true);
        responseWrapper.setStatus("");
        return responseWrapper;
    }

    /**
     * @param news_name
     * @return
     */
    @Override
    public ResponseWrapper searchNewsFeedByName(String news_name) {
        List<NewsFeedDataModel> newsFeedDocuments = MongoHelper
                .find(Query.query(Criteria.where("name").regex(news_name, "i").and("is_deleted").ne(true).and("is_published").is(true)), NewsFeedDataModel.class);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setResult(newsFeedDocuments);
        responseWrapper.setSuccess(true);
        int newsFeedCount = newsFeedDocuments.size();
        responseWrapper.setStatus(String.format("Found %d news feeds", newsFeedCount));
        return responseWrapper;
    }

    /**
     * @param category_name
     * @return
     */
    @Override
    public ResponseWrapper searchCategoryByName(String category_name) {
        List<CategoryDataModel> categoryDocuments = MongoHelper.find(Query.query(Criteria.where("name").regex(category_name,"i").and("is_deleted").ne(true).and("is_published").is(true)), CategoryDataModel.class);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setResult(categoryDocuments);
        responseWrapper.setSuccess(true);
        int categoryListCount = categoryDocuments.size();
        responseWrapper.setStatus(String.format("Found %d Cateogries", categoryListCount));
        return responseWrapper;
    }

    /**
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public ResponseWrapper fetchCategoryUsingLimitAndOffset(int offset, int limit) {
        return null;
    }

    @Override
    public ResponseWrapper getAllData() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<CategoryDataModel> categoryDocuments = MongoHelper.fetchAll(CategoryDataModel.class);
        for(CategoryDataModel categoryDocument : categoryDocuments) {
            List<NewsFeedDataModel> newsList = MongoHelper
                    .find(Query.query(Criteria.where("_id").in(categoryDocument.getNewsListId())), NewsFeedDataModel.class);
            categoryDocument.setNewsList(newsList);
        }
        responseWrapper.setResult(categoryDocuments);
        responseWrapper.setSuccess(true);
        responseWrapper.setStatus("");
        return responseWrapper;
    }
}
