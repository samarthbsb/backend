package com.sv.newsfeeds.services;

import org.springframework.stereotype.Service;

import com.sv.newsfeeds.helpers.MongoHelper;

/**
 * Created by samarth on 30/10/14.
 */
@Service
public class ApiService {
    public void saveObjectToMongo(Object o){
        MongoHelper.save(o);
    }
}
