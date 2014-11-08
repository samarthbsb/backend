package newsfeeds.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samarth on 29/10/14.
 */
public class NewsFeedMetaData {
    public String name;
    public String feedUrl;
    public String thumbnailUrl;
    public boolean isPublished;

    public void fromJsonObject(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        feedUrl = jsonObject.getString("feedUrl");
        thumbnailUrl = jsonObject.getString("thumbnailUrl");
        isPublished = jsonObject.getBoolean("isPublished");
    }
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("feedUrl",feedUrl);
        jsonObject.put("thumbnailUrl",thumbnailUrl);
        jsonObject.put("isPublished",isPublished);
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
