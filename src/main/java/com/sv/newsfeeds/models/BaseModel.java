package com.sv.newsfeeds.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by samarth on 10/11/14.
 */
public class BaseModel {

    @Id
    @Field(value = "id")
    private String id;

    @Field(value = "created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value="created_at")
    private Date createdAt;

    @Field(value = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "updated_at")
    private Date updatedAt;

    @Field(value = "is_published")
    private boolean isPublished;

    @Field(value = "is_deleted")
    private boolean isDeleted;

    @JsonProperty(value = "is_deleted")
    public boolean isDeleted() {
        return isDeleted;
    }

    @JsonProperty(value = "is_deleted")
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @JsonProperty(value="is_published")
    public boolean isPublished() {
        return isPublished;
    }

    @JsonProperty(value = "is_published")
    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    /**
     * Returns the identifier of the document.
     *
     * @return the id
     */

    public String getId() {
        return id;
    }

    @JsonProperty(value="created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(value="created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty(value = "updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty(value = "updated_at")
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object obj) {

        if(this == obj) {
            return true;
        }

        if(this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        BaseModel that = (BaseModel) obj;

        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id == null? 0 : id.hashCode();
    }

}
