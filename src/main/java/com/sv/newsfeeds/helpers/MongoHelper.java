package com.sv.newsfeeds.helpers;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.sv.newsfeeds.config.SpringMongoConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class MongoHelper {
    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    private static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    public static DBCollection getCollection(String collectionName) {
        return mongoOperation.getCollection(collectionName);
    }

    /**
     *
     * @param o
     * @throws MongoException
     */
    public static void save(Object o) throws MongoException {
        mongoOperation.save(o);
    }

    /**
     *
     * @param modelClassObject
     * @param <E>
     * @return
     * @throws MongoException
     */
    public static <E> List<E> fetchAll(Class<E> modelClassObject) throws MongoException{
        List<E> listObjects = mongoOperation.findAll(modelClassObject);
        return listObjects;
    }

    /**
     *
     * @param object
     * @throws MongoException
     */
    public static void deleteByObject(Object object) throws MongoException {
        mongoOperation.remove(object);
    }

    /**
     *
     * @param id
     * @param entityClass
     * @param <E>
     * @throws MongoException
     */
    public static <E> void deleteById(String id, Class<E> entityClass) throws MongoException {
        deleteByObject(findById(id, entityClass));
    }

    /**
     *
     * @param id
     * @param entityClass
     * @param <E>
     * @return
     * @throws MongoException
     */
    public static <E> E findById(String id, Class<E> entityClass) throws MongoException{
        E x = (E) mongoOperation.findById(id, entityClass);
        return x;
    }

    /**
     *
     * @param query
     * @param update
     * @param entityClass
     * @param updateFirst
     * @return
     */
    public static WriteResult update(Query query,Update update, Class<?> entityClass , boolean updateFirst) {
        if(updateFirst){
            return mongoOperation.updateFirst(query,update,entityClass);
        }
        else{
            return mongoOperation.updateMulti(query,update,entityClass);
        }
    }

    /**
     *
     * @param query
     * @param update
     * @param entityClass
     * @return
     */
    public static WriteResult update(Query query, Update update, Class<?> entityClass) {
        return update(query,update,entityClass,false);
    }

    /**
     *
     * @param entityClass
     * @param <E>
     */
    public static <E> void dropCollection(Class<E> entityClass){
        mongoOperation.dropCollection(entityClass);
    }

    /**
     *
     * @param query
     * @param entityClass
     * @param <E>
     * @return
     */
    public static <E> List<E> find(Query query,Class<E> entityClass){
       List<E> listObjects =  mongoOperation.find(query,entityClass);
       return listObjects;
    }
}
