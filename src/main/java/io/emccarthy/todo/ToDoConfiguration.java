package io.emccarthy.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class ToDoConfiguration extends Configuration {

    private String databaseURL;
    private String database;

    @JsonProperty
    public String getDatabaseURL() {
        return databaseURL;
    }

    @JsonProperty
    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    @JsonProperty
    public String getDatabase() {
        return database;
    }

    @JsonProperty
    public void setDatabase(String database) {
        this.database = database;
    }
}
