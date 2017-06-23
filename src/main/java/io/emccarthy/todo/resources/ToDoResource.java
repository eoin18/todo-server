package io.emccarthy.todo.resources;

import java.net.HttpURLConnection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import io.emccarthy.todo.api.ToDoItem;
import io.emccarthy.todo.persistency.ToDoPersistencyLayer;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
public class ToDoResource {

    private ToDoPersistencyLayer toDoPersistencyLayer;

    public ToDoResource(ToDoPersistencyLayer toDoPersistencyLayer) {
        this.toDoPersistencyLayer = Preconditions.checkNotNull(toDoPersistencyLayer,
                "To do resource requires a to do persistency layer");
    }

    @GET
    @Timed
    @Path("/find")
    public ToDoItem queryToDoItemByName(@QueryParam("name") String name) {
        if(Strings.isNullOrEmpty(name)){
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("name parameter is required to query a to do item by name")
                    .build()
            );
        }
        return this.toDoPersistencyLayer.getTodoItemByName(name);
    }

    @GET
    @Timed
    @Path("{id : \\d+}")
    public ToDoItem getToDoItemById(@PathParam("id") String id){
        long longId = Longs.tryParse(id);
        return this.toDoPersistencyLayer.getTodoItemById(longId);
    }

    @POST
    @Timed
    public void saveToDoItem(ToDoItem item) {
        this.toDoPersistencyLayer.saveTodoItem(item);
    }

    @DELETE
    @Timed
    @Path("{id : \\d+}")
    public void deleteToDoItem(@PathParam("id") String id) {
        long longId = Longs.tryParse(id);
        this.toDoPersistencyLayer.removeTodoItemById(longId);
    }

}
