package newsfeeds.models;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by samarth on 17/11/14.
 */

@Document(collection = "newsfeeds")
public class NewsFeedDataModel extends BaseModel {

    @JsonProperty(value = "news_name")
    @Indexed
    private String name;

    @JsonProperty(value = "feed_url")
    @Field(value = "feed_url")
    private String feedUrl;

    @JsonProperty(value = "thumbnail_url")
    @Field(value = "thumbnail_url")
    private String thumbnailUrl;

    private String description;

    @JsonIgnore
    @Indexed
    @Field(value = "category_id")
    String categoryId;

    @JsonIgnore
    public String getCategoryId() {
        return categoryId;
    }

    @JsonIgnore
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NewsFeedDataModel(){
        super.setCreatedAt(new Date());
        super.setUpdatedAt(new Date());
    }

}
