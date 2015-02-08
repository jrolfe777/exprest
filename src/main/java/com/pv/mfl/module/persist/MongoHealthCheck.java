package com.pv.mfl.module.persist;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.mongodb.MongoClient;

public class MongoHealthCheck extends HealthCheck {

    private MongoClient _mongoClient;

    @Inject
    public MongoHealthCheck(MongoManager mongoManager) {
       _mongoClient = mongoManager.providesMongoClient();
    }

    @Override
    protected Result check() throws Exception {
       _mongoClient.getDatabaseNames();
        return Result.healthy();
    }

}
