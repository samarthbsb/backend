package newsfeeds.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class SpringMongoConfig {

    private static String mongoDBURL = "";
    private static String DBName = "backend";
    private static String devMongoHost = "127.0.0.1";
    private static String devMongoPort = "27017";
    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        mongoDBURL = System.getenv("OPENSHIFT_MONGODB_DB_URL");
        if(mongoDBURL==null || mongoDBURL.isEmpty()){
            mongoDBURL="mongodb://"+devMongoHost+":"+devMongoPort+"/";
        }
        System.out.println(mongoDBURL);
        return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(mongoDBURL)), DBName);
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

        return mongoTemplate;

    }
}
