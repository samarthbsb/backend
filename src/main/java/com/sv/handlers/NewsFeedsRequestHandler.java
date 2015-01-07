package com.sv.handlers;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.sv.newsfeeds.helpers.MongoHelper;
import com.sv.newsfeeds.models.CategoryDataModel;
import com.sv.newsfeeds.models.CategoryModel;
import com.sv.newsfeeds.models.NewsFeedDataModel;
import com.sv.newsfeeds.models.NewsFeedMetaData;
import com.sv.newsfeeds.services.ApiService;
import com.sv.newsfeeds.services.CategoryService;
import com.sv.newsfeeds.wrappers.ResponseWrapper;
import com.sv.util.Utils;

@Controller("/api/(v1|v2)/.*")
public class NewsFeedsRequestHandler implements IUrlRequestHandler {

    @Autowired
    ApiService                  apiService;

    @Autowired
    ObjectMapper                mapper;

    @Autowired
    CategoryService             categoryService;

    private static final String contentType = "application/json";
    private Gson                gson        = new Gson();

    @Override
    public HttpResponse handleRequest(String requestUri, String requestPayload, MessageEvent event, HttpRequest request) throws Exception {
        Map<String, List<String>> urlParameters = Utils.getUrlParameters(requestUri);

        if(requestUri.matches("/api/v2/addNewsFeed.*")) {
            String category_id = Utils.getURLParam(urlParameters, "category_id");
            ResponseWrapper responseWrapper = categoryService.addNewsFeedToCategory(requestPayload, category_id);
            return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
        }
        if(request.getMethod().equals(HttpMethod.GET)) {
            if(requestUri.matches("/api/v1/getAllCategories.*")) {
                List<CategoryModel> list = MongoHelper.fetchAll(CategoryModel.class);
                ResponseWrapper responseWrapper = new ResponseWrapper();
                responseWrapper.setResult(list);
                responseWrapper.setSuccess(true);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v1/getCategories.*")) {
                List<CategoryModel> list = MongoHelper.find(query(Criteria.where("newsList.isPublished").in(true)), CategoryModel.class);
                ResponseWrapper responseWrapper = new ResponseWrapper();
                responseWrapper.setResult(list);
                responseWrapper.setSuccess(true);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v1/getCategories.*")) {
                String category_id = Utils.getURLParam(urlParameters, "category_id");
                MongoHelper.deleteById(category_id, CategoryModel.class);
                return Utils.createResponse("{\"deleted\":true}", contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v1/dropDbCollection.*")) {
                String secKey = Utils.getURLParam(urlParameters, "sec-key");
                String responseStr = null;
                if(secKey.equals("killmee")) {
                    MongoHelper.dropCollection(CategoryModel.class);
                    responseStr = "{\"dropped\":true}";
                }
                else {
                    responseStr = "{\"dropped\":false}";
                }
                return Utils.createResponse(responseStr, contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/getPublishedData.*")) {
                ResponseWrapper responseWrapper = categoryService.getPublishedData();
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/getAllData.*")) {
                ResponseWrapper responseWrapper = categoryService.getAllData();
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/searchNews.*")) {
                String query = Utils.getURLParam(urlParameters, "query");
                ResponseWrapper responseWrapper = categoryService.searchNewsFeedByName(query);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/dropDbCollections.*")) {
                ResponseWrapper responseWrapper = new ResponseWrapper();
                String secKey = Utils.getURLParam(urlParameters, "sec-key");
                if(secKey.equals("killmee")) {
                    MongoHelper.dropCollection(CategoryDataModel.class);
                    MongoHelper.dropCollection(NewsFeedDataModel.class);
                    responseWrapper.setStatus("Dropped Collections categories and newsfeeds");
                    responseWrapper.setSuccess(true);
                    responseWrapper.setResult("");
                }
                else {
                    responseWrapper.setStatus("Collections couldn't be dropped");
                    responseWrapper.setSuccess(false);
                    responseWrapper.setResult("");
                }
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
        }
        else if(request.getMethod().equals(HttpMethod.POST)) {
            if(requestUri.matches("/api/v1/add.*")) {
                CategoryModel category = mapper.readValue(requestPayload, CategoryModel.class);
                apiService.saveObjectToMongo(category);
                String responseStr = "{\"success\":true,\"id\":\"" + category.getId() + "\"}";
                return Utils.createResponse(responseStr, contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v1/addNews.*")) {
                String category_id = Utils.getURLParam(urlParameters, "category_id");
                CategoryModel categoryModel = MongoHelper.findById(category_id, CategoryModel.class);
                NewsFeedMetaData newsFeedMetaData = mapper.readValue(requestPayload, NewsFeedMetaData.class);
                categoryModel.getNewsList().add(newsFeedMetaData);
                MongoHelper.save(categoryModel);
                return Utils.createResponse("{\"success\":true}", contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/addCategory.*")) {
                ResponseWrapper responseWrapper = categoryService.createCategory(requestPayload);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/updateCategory.*")) {
                String category_id = Utils.getURLParam(urlParameters, "category_id");
                String name = Utils.getURLParam(urlParameters, "category_name");
                String background_color = Utils.getURLParam(urlParameters, "background_color");
                String font_color = Utils.getURLParam(urlParameters, "font_color");
                ResponseWrapper responseWrapper = new ResponseWrapper();
                if(category_id == null || category_id.trim().isEmpty()) {
                    responseWrapper.setSuccess(false);
                    responseWrapper.setResult(null);
                    responseWrapper.setStatus("Category ID is required");
                    return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
                }
                Update update = new Update();
                if(!name.trim().isEmpty()) {
                    update.set("name", name);
                }
                if(!font_color.trim().isEmpty()) {
                    update.set("font_color", font_color);
                }
                if(!background_color.trim().isEmpty()) {
                    update.set("background_color", background_color);
                }
                responseWrapper = categoryService.updateCategoryDetailsById(category_id, update);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/updateNews.*")) {
                String news_id = Utils.getURLParam(urlParameters, "news_id");
                String name = Utils.getURLParam(urlParameters, "news_name");
                String description = Utils.getURLParam(urlParameters, "description");
                String thumbnailUrl = Utils.getURLParam(urlParameters, "thumbnail_url");
                String feedUrl = Utils.getURLParam(urlParameters, "feed_url");
                ResponseWrapper responseWrapper = new ResponseWrapper();
                if(news_id == null || news_id.trim().isEmpty()) {
                    responseWrapper.setSuccess(false);
                    responseWrapper.setResult(null);
                    responseWrapper.setStatus("News ID is required");
                    return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
                }
                Update update = new Update();
                if(!name.trim().isEmpty()) {
                    update.set("name", name);
                }
                if(!description.trim().isEmpty()) {
                    update.set("description", description);
                }
                if(!thumbnailUrl.trim().isEmpty()) {
                    update.set("thumbnail_url", thumbnailUrl);
                }
                if(!feedUrl.trim().isEmpty()) {
                    update.set("feed_url", feedUrl);
                }
                responseWrapper = categoryService.updateNewsFeedDetailsById(news_id, update);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/deleteCategory.*")) {
                String category_id = Utils.getURLParam(urlParameters, "category_id");
                String secKey = Utils.getURLParam(urlParameters, "sec-key");
                ResponseWrapper responseWrapper = new ResponseWrapper();
                if(!secKey.trim().isEmpty() && secKey.equals("killmee")) {
                    if(!category_id.trim().isEmpty()) {
                        Update update = new Update();
                        update.set("is_deleted", true);
                        responseWrapper = categoryService.updateCategoryDetailsById(category_id, update);
                        return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
                    }
                }
                responseWrapper.setSuccess(false);
                responseWrapper.setStatus("Either Secret Key is not correct or Category Id is empty");
                responseWrapper.setResult(null);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/deleteNews.*")) {
                String news_id = Utils.getURLParam(urlParameters, "news_id");
                String secKey = Utils.getURLParam(urlParameters, "sec-key");
                ResponseWrapper responseWrapper = new ResponseWrapper();
                if(!secKey.trim().isEmpty() && secKey.equals("killmee")) {
                    if(!news_id.trim().isEmpty()) {
                        Update update = new Update();
                        update.set("is_deleted", true);
                        responseWrapper = categoryService.updateNewsFeedDetailsById(news_id, update);
                        return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
                    }
                }
                responseWrapper.setSuccess(false);
                responseWrapper.setStatus("Either Secret Key is not correct or News Id is empty");
                responseWrapper.setResult(null);
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            else if(requestUri.matches("/api/v2/unpublishNews.*")) {
                String news_id = Utils.getURLParam(urlParameters, "news_id");
                ResponseWrapper responseWrapper = new ResponseWrapper();
                if(!news_id.trim().isEmpty()) {
                    responseWrapper = categoryService.unpublishNewsById(news_id);
                }
                else {
                    responseWrapper.setSuccess(false);
                    responseWrapper.setStatus("News Id is empty");
                    responseWrapper.setResult(null);
                }
                return Utils.createResponse(gson.toJson(responseWrapper), contentType, HttpResponseStatus.OK);
            }
            return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        }
        else {
            return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }
        return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
    }

}
