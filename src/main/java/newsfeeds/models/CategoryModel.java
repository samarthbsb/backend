package newsfeeds.models;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by samarth on 29/10/14.
 */
@Document(collection = "category")
@TypeAlias(value = "cat")
public class CategoryModel extends BaseModel{

    @Indexed(unique = true)
    private String category;

    @Indexed(sparse = true)
    private List<NewsFeedMetaData> newsList;

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

}
