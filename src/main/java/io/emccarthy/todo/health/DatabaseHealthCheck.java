package io.emccarthy.todo.health;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.emccarthy.todo.persistency.MongoDBToDoPersistencyLayer;
import org.bson.Document;

public class DatabaseHealthCheck extends HealthCheck {

    private final MongoDatabase database;

    public DatabaseHealthCheck(final MongoDatabase database) {
        this.database = database;
    }

    @Override
    protected Result check() throws Exception {
        MongoCollection<Document> collection = this.database.getCollection(MongoDBToDoPersistencyLayer.TODO);
        return collection != null ? Result.healthy() : Result.unhealthy("Cannot find or create collection in database");
    }
}
