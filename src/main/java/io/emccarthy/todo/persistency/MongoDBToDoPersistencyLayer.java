package io.emccarthy.todo.persistency;

import java.util.concurrent.atomic.AtomicLong;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.emccarthy.todo.api.Status;
import io.emccarthy.todo.api.ToDoItem;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.Document;
import org.joda.time.LocalDate;

public class MongoDBToDoPersistencyLayer implements ToDoPersistencyLayer {

    public static final String TODO = "todo";
    private final MongoDatabase database;
    private AtomicLong idIncrementer;

    public MongoDBToDoPersistencyLayer(MongoDatabase database) {
        this.database = database;
        initIdIncrementer();
    }

    private void initIdIncrementer() {
        MongoCollection<Document> collection = this.database.getCollection(TODO);
        Document id = collection.find().sort(new BsonDocument("_id", new BsonInt32(-1))).limit(1).first();
        if(id == null) {
            this.idIncrementer = new AtomicLong();
        } else {
            this.idIncrementer = new AtomicLong(id.getLong("_id") + 1);
        }
    }

    @Override
    public ToDoItem getTodoItemByName(String name) {
        MongoCollection<Document> todoCollection = this.database.getCollection(TODO);
        Document todoItem = todoCollection.find(Filters.eq("title", name)).first();
        return todoItem == null ? null : documentToToDoItem(todoItem);
    }

    private ToDoItem documentToToDoItem(Document todoItem) {
        return new ToDoItem(todoItem.getLong("_id"),
                todoItem.getString("title"),
                todoItem.getString("description"),
                LocalDate.fromDateFields(todoItem.getDate("dueDate")),
                Status.fromString(todoItem.getString("status")));
    }

    @Override
    public ToDoItem getTodoItemById(long id) {
        MongoCollection<Document> todoCollection = this.database.getCollection(TODO);
        Document todoItem = todoCollection.find(Filters.eq("_id", id)).first();
        return todoItem == null ? null : documentToToDoItem(todoItem);
    }

    @Override
    public ToDoItem saveTodoItem(ToDoItem toDoItem) {
        ToDoItem saveItem = new ToDoItem(toDoItem.get_id() == 0L ? idIncrementer.getAndIncrement() : toDoItem.get_id(),
                toDoItem.getTitle(), toDoItem.getDescription(), toDoItem.getDueDate(), toDoItem.getStatus());
        MongoCollection<Document> todoCollection = this.database.getCollection(TODO);
        Document newDocument = new Document("_id", saveItem.get_id())
                .append("title", saveItem.getTitle())
                .append("description", saveItem.getDescription())
                .append("dueDate", saveItem.getDueDate().toDate())
                .append("status", saveItem.getStatus().name());
        if(todoCollection.find(Filters.eq("_id", saveItem.get_id())).first() != null){
            removeTodoItemById(saveItem.get_id());
        }
        todoCollection.insertOne(newDocument);
        return saveItem;
    }

    @Override
    public void removeTodoItemById(long id) {
        MongoCollection<Document> collection = this.database.getCollection(TODO);
        collection.deleteOne(Filters.eq("_id", id));
    }
}
