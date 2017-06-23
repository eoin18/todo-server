package io.emccarthy.todo.persistency;

import io.emccarthy.todo.api.ToDoItem;

public interface ToDoPersistencyLayer {

    ToDoItem getTodoItemByName(String name);

    ToDoItem getTodoItemById(long id);

    ToDoItem saveTodoItem(ToDoItem toDoItem);

    void removeTodoItemById(long id);

}
