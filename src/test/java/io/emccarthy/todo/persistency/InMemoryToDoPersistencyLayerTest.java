package io.emccarthy.todo.persistency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.Instant;

import io.emccarthy.todo.api.Status;
import io.emccarthy.todo.api.ToDoItem;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

public class InMemoryToDoPersistencyLayerTest {

    private static final String POPULATED_NAME = "test1";
    private static final String NON_POPULATED_NAME = "test2";
    private static final long POPULATED_ID = 123L;
    private static LocalDate now;

    @Mock
    private ToDoPersistencyLayer mockDelegatePersistencyLayer;

    private ToDoItem testToDoItem;
    private ToDoPersistencyLayer inMemoryToDoPersistencyLayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        now = LocalDate.fromDateFields(Date.from(Instant.now()));
        this.testToDoItem = new ToDoItem(POPULATED_ID, POPULATED_NAME, "This is a test", now, Status.IN_PROGRESS);

        when(this.mockDelegatePersistencyLayer.getTodoItemByName(POPULATED_NAME)).thenReturn(this.testToDoItem);
        when(this.mockDelegatePersistencyLayer.getTodoItemById(POPULATED_ID)).thenReturn(this.testToDoItem);
        when(this.mockDelegatePersistencyLayer.getTodoItemByName(NON_POPULATED_NAME)).thenReturn(null);
        when(this.mockDelegatePersistencyLayer.saveTodoItem(any(ToDoItem.class))).then((InvocationOnMock invocationOnMock) -> invocationOnMock.getArgument(0));
        this.inMemoryToDoPersistencyLayer = new InMemoryToDoPersistencyLayer(this.mockDelegatePersistencyLayer);
    }

    @Test
    public void testSaveToDoItemSavesInDelegateAndCaches() throws Exception {
        ToDoItem saveItem = new ToDoItem(321L, "test3", "this is also a test", now, Status.OVERDUE);
        this.inMemoryToDoPersistencyLayer.saveTodoItem(saveItem);
        verify(this.mockDelegatePersistencyLayer, times(1)).saveTodoItem(saveItem);
        ToDoItem todoItemById = this.inMemoryToDoPersistencyLayer.getTodoItemById(321L);
        assertEquals(saveItem, todoItemById);
        verify(this.mockDelegatePersistencyLayer, never()).getTodoItemById(anyLong());
        verify(this.mockDelegatePersistencyLayer, never()).getTodoItemByName(anyString());
    }

    @Test
    public void testGetReturnsAndPopulatesCache() throws Exception {
        ToDoItem todoItemById = this.inMemoryToDoPersistencyLayer.getTodoItemById(POPULATED_ID);
        assertEquals(this.testToDoItem, todoItemById);
        verify(this.mockDelegatePersistencyLayer, times(1)).getTodoItemById(POPULATED_ID);
        verify(this.mockDelegatePersistencyLayer, never()).getTodoItemByName(anyString());
        todoItemById = this.inMemoryToDoPersistencyLayer.getTodoItemById(POPULATED_ID);
        assertEquals(this.testToDoItem, todoItemById);
        verify(this.mockDelegatePersistencyLayer, times(1)).getTodoItemById(POPULATED_ID);
        verify(this.mockDelegatePersistencyLayer, never()).getTodoItemByName(anyString());
    }

    @Test
    public void testGetReturnsAndPopulatesCacheByName() throws Exception {
        ToDoItem todoItemByName = this.inMemoryToDoPersistencyLayer.getTodoItemByName(POPULATED_NAME);
        assertEquals(this.testToDoItem, todoItemByName);
        verify(this.mockDelegatePersistencyLayer, times(1)).getTodoItemByName(POPULATED_NAME);
        verify(this.mockDelegatePersistencyLayer, never()).getTodoItemById(anyLong());
        todoItemByName = this.inMemoryToDoPersistencyLayer.getTodoItemByName(POPULATED_NAME);
        assertEquals(this.testToDoItem, todoItemByName);
        verify(this.mockDelegatePersistencyLayer, times(1)).getTodoItemByName(POPULATED_NAME);
        verify(this.mockDelegatePersistencyLayer, never()).getTodoItemById(anyLong());
    }

    @Test
    public void testGetReturnsNullForNonPopulatedItem() throws Exception {
        ToDoItem todoItemByName = this.inMemoryToDoPersistencyLayer.getTodoItemByName(NON_POPULATED_NAME);
        assertNull(todoItemByName);
    }

    @Test
    public void testSaveAndRemoveItemRemovesItemFromDelegateAndCaches() throws Exception {
        ToDoItem saveItem = new ToDoItem(321L, "test3", "this is also a test", now, Status.OVERDUE);
        this.inMemoryToDoPersistencyLayer.saveTodoItem(saveItem);
        ToDoItem todoItemById = this.inMemoryToDoPersistencyLayer.getTodoItemById(321L);
        assertEquals(saveItem, todoItemById);
        this.inMemoryToDoPersistencyLayer.removeTodoItemById(321L);
        todoItemById = this.inMemoryToDoPersistencyLayer.getTodoItemById(321L);
        assertNull(todoItemById);
        verify(this.mockDelegatePersistencyLayer, times(1)).removeTodoItemById(321L);
    }

}