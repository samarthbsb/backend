package newsfeeds.services;

import newsfeeds.helpers.MongoHelper;

/**
 * Created by samarth on 30/10/14.
 */
public class ApiService {
    public void saveObjectToMongo(Object o){
        MongoHelper.save(o);
    }
}
