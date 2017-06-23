package io.emccarthy.todo.persistency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.util.Date;

import io.emccarthy.todo.AbstractMongoDBTest;
import io.emccarthy.todo.api.Status;
import io.emccarthy.todo.api.ToDoItem;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MongoDBToDoPersistencyLayerTest extends AbstractMongoDBTest {

    private static LocalDate now;

    private ToDoPersistencyLayer mongoDbToDoPersistencyLayer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        now = LocalDate.fromDateFields(Date.from(Instant.now()));
        this.mongoDbToDoPersistencyLayer = new MongoDBToDoPersistencyLayer(mongoDatabase);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testSaveToDoItem() throws Exception {
        ToDoItem item = new ToDoItem(123L, "test", "this is a test", now, Status.OVERDUE);
        this.mongoDbToDoPersistencyLayer.saveTodoItem(item);
        ToDoItem todoItemById = this.mongoDbToDoPersistencyLayer.getTodoItemById(123L);
        assertEquals(item, todoItemById);
    }

    @Test
    public void testSaveToDoItemAndGetByName() throws Exception {
        ToDoItem item = new ToDoItem(123L, "test", "this is a test", now, Status.OVERDUE);
        this.mongoDbToDoPersistencyLayer.saveTodoItem(item);
        ToDoItem todoItemByName = this.mongoDbToDoPersistencyLayer.getTodoItemByName("test");
        assertEquals(item, todoItemByName);
    }

    @Test
    public void testUpdateToDoItem() throws Exception {
        ToDoItem item = new ToDoItem(123L, "test", "this is a test", now, Status.OVERDUE);
        this.mongoDbToDoPersistencyLayer.saveTodoItem(item);
        ToDoItem todoItemById = this.mongoDbToDoPersistencyLayer.getTodoItemById(123L);
        assertEquals(item, todoItemById);
        item = new ToDoItem(123L, "newTest", "update: this is still a test", now, Status.COMPLETE);
        this.mongoDbToDoPersistencyLayer.saveTodoItem(item);
        todoItemById = this.mongoDbToDoPersistencyLayer.getTodoItemById(123L);
        assertEquals(item, todoItemById);
    }

    @Test
    public void testRemoveToDoItem() throws Exception {
        ToDoItem item = new ToDoItem(123L, "test", "this is a test", now, Status.OVERDUE);
        this.mongoDbToDoPersistencyLayer.saveTodoItem(item);
        ToDoItem todoItemById = this.mongoDbToDoPersistencyLayer.getTodoItemById(123L);
        assertEquals(item, todoItemById);
        this.mongoDbToDoPersistencyLayer.removeTodoItemById(123L);
        todoItemById = this.mongoDbToDoPersistencyLayer.getTodoItemById(123L);
        assertNull(todoItemById);
    }

}