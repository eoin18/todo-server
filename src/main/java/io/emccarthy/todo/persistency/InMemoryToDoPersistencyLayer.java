package io.emccarthy.todo.persistency;

import java.util.concurrent.ConcurrentHashMap;

import io.emccarthy.todo.api.ToDoItem;

public class InMemoryToDoPersistencyLayer implements ToDoPersistencyLayer {

    private final ToDoPersistencyLayer delegatePersistencyLayer;
    private ConcurrentHashMap<String, ToDoItem> nameToToDoItemMap;
    private ConcurrentHashMap<Long, ToDoItem> idToToDoItemMap;

    public InMemoryToDoPersistencyLayer(final ToDoPersistencyLayer delegatePersistencyLayer){
        this.delegatePersistencyLayer = delegatePersistencyLayer;
        this.nameToToDoItemMap = new ConcurrentHashMap<>();
        this.idToToDoItemMap = new ConcurrentHashMap<>();
    }

    @Override
    public ToDoItem getTodoItemByName(String name) {
        ToDoItem toDoItem = this.nameToToDoItemMap.get(name);
        if(toDoItem == null) {
            toDoItem = this.delegatePersistencyLayer.getTodoItemByName(name);
            if(toDoItem != null) {
                insertIntoCache(toDoItem);
            }
        }
        return toDoItem;
    }

    @Override
    public ToDoItem getTodoItemById(long id) {
        ToDoItem toDoItem = this.idToToDoItemMap.get(id);
        if(toDoItem == null) {
            toDoItem = this.delegatePersistencyLayer.getTodoItemById(id);
            if(toDoItem != null) {
                insertIntoCache(toDoItem);
            }
        }
        return toDoItem;
    }

    private void insertIntoCache(ToDoItem toDoItem) {
        this.idToToDoItemMap.put(toDoItem.get_id(), toDoItem);
        this.nameToToDoItemMap.put(toDoItem.getTitle(), toDoItem);
    }

    @Override
    public ToDoItem saveTodoItem(ToDoItem toDoItem) {
        ToDoItem saveItem = this.delegatePersistencyLayer.saveTodoItem(toDoItem);
        insertIntoCache(saveItem);
        return saveItem;
    }

    @Override
    public void removeTodoItemById(long id) {
        this.delegatePersistencyLayer.removeTodoItemById(id);
        ToDoItem removedItem = this.idToToDoItemMap.remove(id);
        if(removedItem != null) {
            this.nameToToDoItemMap.remove(removedItem.getTitle());
        }
    }
}
