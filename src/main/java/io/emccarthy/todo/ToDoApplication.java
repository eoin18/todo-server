package io.emccarthy.todo;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.emccarthy.todo.health.DatabaseHealthCheck;
import io.emccarthy.todo.persistency.InMemoryToDoPersistencyLayer;
import io.emccarthy.todo.persistency.MongoDBToDoPersistencyLayer;
import io.emccarthy.todo.persistency.ToDoPersistencyLayer;
import io.emccarthy.todo.resources.ToDoResource;

public class ToDoApplication extends Application<ToDoConfiguration> {

    private static final String TODO_SERVER = "todo-server";

    public static void main(String[] args) throws Exception {
        new ToDoApplication().run(args);
    }

    @Override
    public String getName() {
        return TODO_SERVER;
    }

    @Override
    public void initialize(Bootstrap<ToDoConfiguration> bootstrap) {
    }

    @Override
    public void run(ToDoConfiguration configuration, Environment environment) throws Exception {
        MongoClientURI connectionString = new MongoClientURI(configuration.getDatabaseURL());
        MongoClient client = new MongoClient(connectionString);
        MongoDatabase database = client.getDatabase(configuration.getDatabase());
        ToDoPersistencyLayer mongoDbToDoPersistencyLayer = new MongoDBToDoPersistencyLayer(database);
        InMemoryToDoPersistencyLayer inMemoryToDoPersistencyLayer = new InMemoryToDoPersistencyLayer(mongoDbToDoPersistencyLayer);

        HealthCheck databaseHealthCheck = new DatabaseHealthCheck(database);

        final ToDoResource resource = new ToDoResource(inMemoryToDoPersistencyLayer);

        environment.healthChecks().register("mongodb health check", databaseHealthCheck);
        environment.jersey().register(resource);
    }
}
