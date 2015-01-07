package com.sv.newsfeeds.models;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by samarth on 13/11/14.
 */

@Document(collection = "categories")
public class CategoryDataModel extends BaseModel {

    @Indexed(unique = true)
    @JsonProperty(value = "category_name")
    private String name;

    @JsonProperty(value = "background_color")
    @Field(value = "background_color")
    private String backgroundColor;

    @JsonProperty(value = "font_color")
    @Field(value = "font_color")
    private String fontColor;

    @JsonIgnore
    private List<String> newsListId;

    public CategoryDataModel() {
        super.setIsPublished(true);
        super.setIsDeleted(false);
        super.setCreatedAt(new Date());
        super.setUpdatedAt(new Date());
        setBackgroundColor("");
        setFontColor("");
        this.newsListId = new ArrayList<String>();
    }

    @JsonIgnore
    public List<String> getNewsListId() {
        return newsListId;
    }

    @JsonIgnore
    public void setNewsListId(List<String> newsListId) {
        this.newsListId = newsListId;
    }

    @Transient
    List<NewsFeedDataModel> newsList;

    public List<NewsFeedDataModel> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsFeedDataModel> newsList) {
        this.newsList = newsList;
    }

    public String getName() {
        return name;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setName(String name) {
        this.name = name;
    }
}
