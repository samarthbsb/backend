package newsfeeds.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samarth on 29/10/14.
 */
@Document(collection = "category")
@TypeAlias(value = "cat")
public class CategoryModel {
    @Id
    private String id;

    @Indexed(unique = true)
    private String category;

    @Indexed(sparse = true)
    private List<NewsFeedMetaData> newsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<NewsFeedMetaData> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsFeedMetaData> newsList) {
        this.newsList = newsList;
    }

    public void fromJsonObject(JSONObject jsonObject) throws JSONException {
        setCategory(jsonObject.getString("category"));

        JSONArray newsJsonArray = jsonObject.getJSONArray("newsList");
        List<NewsFeedMetaData> newsMetaList = new ArrayList<NewsFeedMetaData>();
        int length = newsJsonArray.length();
        for(int i=0;i<length;i++){
            NewsFeedMetaData newsMeta = new NewsFeedMetaData();
            newsMeta.fromJsonObject((JSONObject) newsJsonArray.get(i));
            newsMetaList.add(newsMeta);
        }
        setNewsList(newsMetaList);

        setId(null);
        if(jsonObject.has("id") && !jsonObject.getString("id").trim().isEmpty()){
            setId(jsonObject.getString("id"));
        }
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("category",this.category);
        JSONArray jsonArray=new JSONArray();
        for(NewsFeedMetaData newsFeedMetaData:this.getNewsList()){
            jsonArray.put(newsFeedMetaData.toJsonObject());
        }
        jsonObject.put("newsList",jsonArray);
        return jsonObject;
    }

    public String toJsonString() throws JSONException {
        return toJsonObject().toString();
    }
    public void fromJsonString(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        fromJsonObject(jsonObject);
    }
}
