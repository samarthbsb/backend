package newsfeeds.controller;

import newsfeeds.helpers.MongoHelper;
import newsfeeds.models.CategoryModel;
import newsfeeds.models.NewsFeedMetaData;
import newsfeeds.wrappers.ResponseWrapper;
import newsfeeds.services.ApiService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by samarth on 08/11/14.
 */
@Controller
@RequestMapping(value = { "api/v1" })
public class ApiController {

    @Autowired
    ApiService apiService;

    @Autowired
    ObjectMapper mapper;

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/getAllCategories" }, method = RequestMethod.GET)
    public void getAllCategories(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CategoryModel> list = MongoHelper.fetchAll(CategoryModel.class);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setResult(list);
        responseWrapper.setSuccess(true);
//        responseWrapper.setCount(list.size());
        response.setContentType("application/json");
        response.getOutputStream().write("ok".getBytes());
        mapper.writeValue(response.getWriter(), responseWrapper);
    }

    @RequestMapping(value = { "/getCategories" }, method = RequestMethod.GET)
    public void getPublishedCategories(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CategoryModel> list = MongoHelper.find(query(Criteria.where("newsList.isPublished").in(true)), CategoryModel.class);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setResult(list);
        responseWrapper.setSuccess(true);
//        responseWrapper.setCount(list.size());
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), responseWrapper);
    }

    /**
     * @param request
     * @param response
     * @param requestBody
     * @throws IOException
     */
    @RequestMapping(value = { "/add" }, method = RequestMethod.POST, headers = "Accept=*")
    @ResponseBody
    public void addObject(HttpServletRequest request, HttpServletResponse response,
            @RequestBody String requestBody) throws IOException {
        CategoryModel category = mapper.readValue(requestBody, CategoryModel.class);
        apiService.saveObjectToMongo(category);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true,\"id\":\"" + category.getId() + "\"}");
    }

    /**
     * @param request
     * @param response
     * @param requestBody
     * @param category_id
     * @throws IOException
     */
    @RequestMapping(value = { "/addNews" }, method = RequestMethod.POST, headers = "Accept=*")
    @ResponseBody
    public void addToNewsListByCatId(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBody,
            @RequestParam(value = "category_id", defaultValue = "") String category_id) throws IOException {

        CategoryModel categoryModel = MongoHelper.findById(category_id, CategoryModel.class);
        NewsFeedMetaData newsFeedMetaData = mapper.readValue(requestBody, NewsFeedMetaData.class);
        categoryModel.getNewsList().add(newsFeedMetaData);
        MongoHelper.save(categoryModel);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true}");
    }

    /**
     * @param request
     * @param response
     * @param category_id
     * @throws IOException
     */
    @RequestMapping(value = { "/deleteCategory" }, method = RequestMethod.GET)
    public void deleteCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "category_id", defaultValue = "") String category_id) throws IOException {
        MongoHelper.deleteById(category_id, CategoryModel.class);
        response.setContentType("application/json");
        response.getWriter().write("{\"deleted\":true}");

    }

    @RequestMapping(value = { "/dropDbCollection" }, method = RequestMethod.GET)
    public void dropCategoryCollection(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "sec-key", defaultValue = "") String secKey) throws IOException {
        response.setContentType("application/json");
        if(secKey.equals("killmee")) {
            MongoHelper.dropCollection(CategoryModel.class);
            response.getWriter().write("{\"dropped\":true}");
        }
        else {
            response.getWriter().write("{\"dropped\":false}");
        }

    }

}
