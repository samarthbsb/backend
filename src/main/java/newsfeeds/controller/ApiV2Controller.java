package newsfeeds.controller;

import newsfeeds.Utils.Utils;
import newsfeeds.helpers.MongoHelper;
import newsfeeds.models.CategoryDataModel;
import newsfeeds.models.NewsFeedDataModel;
import newsfeeds.services.CategoryService;
import newsfeeds.services.CategoryServiceImpl;
import newsfeeds.wrappers.ResponseWrapper;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by samarth on 17/11/14.
 */
@Controller
@RequestMapping(value = "api/v2")
public class ApiV2Controller {

    CategoryService categoryService = new CategoryServiceImpl();

    /**
     * @param request
     * @param response
     * @param content
     * @throws IOException
     */
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ResponseBody
    public void addCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody String content) throws IOException {
        ResponseWrapper responseWrapper = categoryService.createCategory(content);
        response.setContentType("application/json");
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @param content
     * @param category_id
     * @throws IOException
     */
    @RequestMapping(value = "/addNewsFeed", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void addNews(HttpServletRequest request, HttpServletResponse response,
            @RequestBody String content,
            @RequestParam(value = "category_id") String category_id) throws IOException {
        ResponseWrapper responseWrapper = categoryService.addNewsFeedToCategory(content, category_id);
        response.setContentType("application/json");
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/getPublishedData", method = RequestMethod.GET)
    public void getPublishedNewsFeeds(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseWrapper responseWrapper = categoryService.getPublishedData();
        response.setContentType("application/json");
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/getAllData", method = RequestMethod.GET)
    public void getAllNewsFeeds(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseWrapper responseWrapper = categoryService.getAllData();
        response.setContentType("application/json");
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @param query
     * @throws IOException
     */
    @RequestMapping(value = "/searchNews", method = RequestMethod.GET)
    public void searchNews(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "query") String query) throws IOException {
        ResponseWrapper responseWrapper = categoryService.searchNewsFeedByName(query);
        response.setContentType("application/json");
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @param category_id
     * @param name
     * @param background_color
     * @param font_color
     * @throws IOException
     */
    @RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
    public void updateCategory(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "category_id", defaultValue = "") String category_id,
            @RequestParam(value = "category_name", defaultValue = "") String name,
            @RequestParam(value = "background_color", defaultValue = "") String background_color,
            @RequestParam(value = "font_color", defaultValue = "") String font_color) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        response.setContentType("application/json");
        if(category_id == null || category_id.trim().isEmpty()) {
            responseWrapper.setSuccess(false);
            responseWrapper.setResult(null);
            responseWrapper.setStatus("Category ID is required");
            Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
            return;
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
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @param news_id
     * @param name
     * @param description
     * @param thumbnailUrl
     * @param feedUrl
     * @throws IOException
     */
    @RequestMapping(value = "/updateNews", method = RequestMethod.POST)
    public void updateNews(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "news_id", defaultValue = "") String news_id,
            @RequestParam(value = "news_name", defaultValue = "") String name,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "thumbnail_url", defaultValue = "") String thumbnailUrl,
            @RequestParam(value = "feed_url", defaultValue = "") String feedUrl) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        response.setContentType("application/json");
        if(news_id == null || news_id.trim().isEmpty()) {
            responseWrapper.setSuccess(false);
            responseWrapper.setResult(null);
            responseWrapper.setStatus("News ID is required");
            Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
            return;
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
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     *
     * @param request
     * @param response
     * @param category_id
     * @param secKey
     * @throws IOException
     */
    @RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
    public void deleteCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "category_id", defaultValue = "") String category_id,
            @RequestParam(value = "sec-key") String secKey)
            throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        response.setContentType("application/json");
        if(!secKey.trim().isEmpty() && secKey.equals("killmee")) {
            if(!category_id.trim().isEmpty()) {
                Update update = new Update();
                update.set("is_deleted", true);
                responseWrapper = categoryService.updateCategoryDetailsById(category_id, update);
                Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
                return;
            }
        }
        responseWrapper.setSuccess(false);
        responseWrapper.setStatus("Either Secret Key is not correct or Category Id is empty");
        responseWrapper.setResult(null);
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     *
     * @param request
     * @param response
     * @param news_id
     * @param secKey
     * @throws IOException
     */
    @RequestMapping(value = "/deleteNews", method = RequestMethod.POST)
    public void deleteNews(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "news_id", defaultValue = "") String news_id, @RequestParam(value = "sec-key") String secKey)
            throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        response.setContentType("application/json");
        if(!secKey.trim().isEmpty() && secKey.equals("killmee")) {
            if(!news_id.trim().isEmpty()) {
                Update update = new Update();
                update.set("is_deleted", true);
                responseWrapper = categoryService.updateNewsFeedDetailsById(news_id, update);
                Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
                return;
            }
        }
        responseWrapper.setSuccess(false);
        responseWrapper.setStatus("Either Secret Key is not correct or News Id is empty");
        responseWrapper.setResult(null);
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     *
     * @param request
     * @param response
     * @param news_id
     * @throws IOException
     */
    @RequestMapping(value = "/unpublishNews", method = RequestMethod.POST)
    public void deleteNews(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "news_id", defaultValue = "") String news_id)
            throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        response.setContentType("application/json");
        if(!news_id.trim().isEmpty()) {
            responseWrapper = categoryService.unpublishNewsById(news_id);
        }
        else {
            responseWrapper.setSuccess(false);
            responseWrapper.setStatus("News Id is empty");
            responseWrapper.setResult(null);
        }
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }

    /**
     *
     * @param request
     * @param response
     * @param secKey
     * @throws IOException
     */
    @RequestMapping(value = { "/dropDbCollections" }, method = RequestMethod.GET)
    public void dropCategoryCollection(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "sec-key", defaultValue = "") String secKey) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        response.setContentType("application/json");
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
        Utils.getObjectMapper().writeValue(response.getOutputStream(), responseWrapper);
    }
}
