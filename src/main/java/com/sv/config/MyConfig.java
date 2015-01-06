package com.sv.config;

import com.sv.db.MongoDBManager;
import com.sv.util.ConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * @author vaibhav
 */
@Configuration
public class MyConfig {

    private static final Logger logger = Logger.getLogger(MyConfig.class.getCanonicalName());
    @Autowired
    private ConfigFile properties;

    //    @Autowired
    //    MongoDbFactory       musicDbFactory;
    //
    //    @Autowired
    //    MongoDbFactory       seDbFactory;
    private MongoDBConfig getMongoDBConfig(String propertyName, String defaultValue) {
        MongoDBConfig config = new MongoDBConfig();
        config.setMongodbHost(properties.getStringProperty("mongodb.host", "127.0.0.1"));
        config.setMongodbPort(properties.getIntProperty("mongodb.port", 27017));
        config.setMongoDBName(properties.getStringProperty(propertyName, defaultValue));
        config.setMongoDBPrefix(properties.getStringProperty("mongodb.prefix", ""));
        config.setMongodbLoggingEnabled(properties.getBooleanProperty("mongodb.loggingenabled", false));
        return config;

    }

    @Bean(name = "mongoRailDBManager")
    public MongoDBManager mongoRailDBManager() {
        return new MongoDBManager(getMongoDBConfig("mongodb.raildb", "rail"));
    }

    @Bean
    public NettyConfig nettyConfig() {
        NettyConfig config = new NettyConfig();
        config.setHttpport(properties.getIntProperty("server.httpport", 8181));
        config.setBrokerId(properties.getIntProperty("server.brokerid", 0));
        config.setHostName(properties.getStringProperty("server.hostname", null));

        try {
            if(config.getHostName() == null || config.getHostName().isEmpty()) {
                config.setHostName(InetAddress.getLocalHost().getHostName());
            }
            config.setHostAddr(InetAddress.getLocalHost().getHostAddress());
        }
        catch (UnknownHostException e) {
            System.err.println("Unable to find hostname : " + e.getMessage());
            config.setHostName("localhost-" + config.getBrokerId());
        }

        config.setNumThreads(properties.getIntProperty("server.threads", (Runtime.getRuntime().availableProcessors() * 2 + 1)));
        config.setCorePoolSize(properties.getIntProperty("server.corePoolSize", 10));
        config.setMaxPoolSize(properties.getIntProperty("server.maxPoolSize", 100));
        return config;
    }
}
