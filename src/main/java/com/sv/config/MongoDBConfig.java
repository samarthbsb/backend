package com.sv.config;

public class MongoDBConfig {

    public static final String PHOTO_DB_KEY = "galleries";
    public static final String NEWS_DB_KEY = "news";
    public static final String STAR_PROFILE_DB_KEY = "profiles";
    public static final String MONITORING = "monitoring";
    public static final String PHOTO_STATS_KEY = "photostats";
    public static final String TOP_TEN_KEY = "topten";

    // mongo db properties
    private String mongodbHost;
    private int mongodbPort;
    private String mongoDBName;
    private String mongoDBPrefix;
    private Boolean mongodbLoggingEnabled;

    public String getMongodbHost() {
        return mongodbHost;
    }

    public void setMongodbHost(String mongodbHost) {
        this.mongodbHost = mongodbHost;
    }

    public int getMongodbPort() {
        return mongodbPort;
    }

    public void setMongodbPort(int mongodbPort) {
        this.mongodbPort = mongodbPort;
    }

    public String getMongoDBPrefix() {
        return mongoDBPrefix;
    }

    public void setMongoDBPrefix(String mongoDBPrefix) {
        this.mongoDBPrefix = mongoDBPrefix;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }

    public void setMongoDBName(String mongoDBName) {
        this.mongoDBName = mongoDBName;
    }
    
    public Boolean getMongodbLoggingEnabled() {
        return mongodbLoggingEnabled;
    }
    
    public void setMongodbLoggingEnabled(Boolean mongodbLoggingEnabled) {
        this.mongodbLoggingEnabled = mongodbLoggingEnabled;
    }
}
