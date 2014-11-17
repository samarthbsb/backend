package newsfeeds.services;

import newsfeeds.wrappers.ResponseWrapper;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by samarth on 17/11/14.
 */
public interface CategoryService {

    public ResponseWrapper createCategory(String jsonData);

    public ResponseWrapper addNewsFeedToCategory(String jsonData, String category_id);

    public ResponseWrapper updateCategoryDetailsById(String category_id, Update update);

    public ResponseWrapper updateNewsFeedDetailsById(String news_id, Update update);

    public ResponseWrapper deleteCategoryById(String category_id);

    public ResponseWrapper deleteNewsById(String news_id);

    public ResponseWrapper unpublishNewsById(String news_id);

    public ResponseWrapper unpublishCategoryById(String category_id);

    public ResponseWrapper getPublishedData();

    public ResponseWrapper searchNewsFeedByName(String news_name);

    public ResponseWrapper searchCategoryByName(String category_name);

    public ResponseWrapper fetchCategoryUsingLimitAndOffset(int offset,int limit);

    public ResponseWrapper getAllData();

}
