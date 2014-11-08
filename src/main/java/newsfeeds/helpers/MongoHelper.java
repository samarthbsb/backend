package newsfeeds.helpers;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import newsfeeds.config.SpringMongoConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

public class MongoHelper {
    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    private static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    public static DBCollection getCollection(String collectionName) {
        return mongoOperation.getCollection(collectionName);
    }

    public static void save(Object o) throws MongoException {
        mongoOperation.save(o);
    }

    public static <E> List<E> fetchAll(Class<E> modelClassObject) throws MongoException{
        List<E> listObjects = mongoOperation.findAll(modelClassObject);
        return listObjects;
    }

    public static void deleteByObject(Object object) throws MongoException {
        mongoOperation.remove(object);
    }

    public static <E> void deleteById(String id, Class<E> entityClass) throws MongoException {
        deleteByObject(findById(id, entityClass));
    }

    public static <E> E findById(String id, Class<E> entityClass) throws MongoException{
        E x = (E) mongoOperation.findById(id, entityClass);
        return x;
    }
}
