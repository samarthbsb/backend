package newsfeeds.controller;

import newsfeeds.helpers.MongoHelper;
import newsfeeds.models.CategoryModel;
import newsfeeds.models.NewsFeedMetaData;
import newsfeeds.services.ApiService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by samarth on 08/11/14.
 */
@Controller
@RequestMapping(value = {"api/v1"})
public class ApiController {
    @RequestMapping(value = {"/getAll"}, method = RequestMethod.GET)
    public void getAll(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/json");
        response.getWriter().write("{\"success\":\"Ok\"}");
    }
    @RequestMapping(value = {"/getTest"}, method = RequestMethod.GET)
    public void getTest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/json");
        response.getWriter().write("{\"success\":\"dasldakdladkk\"}");
    }
    @Autowired
    ApiService apiService;
    //Logger logger = LoggerFactory.getLogger(ApiController.class);
    @RequestMapping(value = {"/getCategories"},method = RequestMethod.GET)
    public void getCategories(HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
        //logger.info("INFO","Called api getCategories");
        List<CategoryModel> list = MongoHelper.fetchAll(CategoryModel.class);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(CategoryModel categoryModel:list){
            jsonArray.put(categoryModel.toJsonObject());
        }
        jsonObject.put("result",jsonArray);
        jsonObject.put("count", jsonArray.length());
        response.setContentType("text/json");
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping(value = {"/add"},method = RequestMethod.POST,headers = "Accept=*")
    @ResponseBody
    public void addObject(HttpServletRequest request,HttpServletResponse response,@RequestBody String requestBody) throws IOException {
        CategoryModel cat = new CategoryModel();
        try {
            System.out.println(requestBody);
            cat.fromJsonString(requestBody);
            apiService.saveObjectToMongo(cat);
            response.getWriter().write("{\"success\":true,\"id\":\""+cat.getId()+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = {"/addNews"},method = RequestMethod.POST,headers = "Accept=*")
    @ResponseBody
    public void addToNewsListByCatId(HttpServletRequest request,HttpServletResponse response,@RequestBody String requestBody,@RequestParam(value = "category_id",defaultValue = "") String category_id) throws JSONException, IOException {
        CategoryModel categoryModel = MongoHelper.findById(category_id,CategoryModel.class);
        NewsFeedMetaData newsFeedMetaData = new NewsFeedMetaData();
        newsFeedMetaData.fromJsonString(requestBody);
        categoryModel.getNewsList().add(newsFeedMetaData);
        MongoHelper.save(categoryModel);
        response.getWriter().write("{\"success\":true}");
    }
}
