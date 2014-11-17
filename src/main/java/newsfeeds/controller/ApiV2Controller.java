package newsfeeds.controller;

import newsfeeds.Utils.Utils;
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
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST, headers = "Accept=*")
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
    @RequestMapping(value = "/addNewsFeed", method = RequestMethod.POST, headers = "Accept=*")
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
    @RequestMapping(value = "/updateCategory", method = RequestMethod.GET)
    public void updateCategory(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "category_id") String category_id,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "background_color", defaultValue = "") String background_color,
            @RequestParam(value = "font_color", defaultValue = "") String font_color) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
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
    @RequestMapping(value = "/updateNews", method = RequestMethod.GET)
    public void updateNews(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "news_id") String news_id,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "thumbnailUrl", defaultValue = "") String thumbnailUrl,
            @RequestParam(value = "feedUrl", defaultValue = "") String feedUrl) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
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

}
