package io.emccarthy.todo.resources;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import io.emccarthy.todo.AbstractMongoDBTest;
import io.emccarthy.todo.api.Status;
import io.emccarthy.todo.api.ToDoItem;
import io.emccarthy.todo.persistency.MongoDBToDoPersistencyLayer;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class ToDoResourceTest extends AbstractMongoDBTest{

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ToDoResource(new MongoDBToDoPersistencyLayer(mongoDatabase)))
            .build();
    private ToDoItem instanceOne;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ToDoItem.DATE_FORMAT, Locale.getDefault());
        LocalDate dueDate = LocalDate.fromDateFields(simpleDateFormat.parse("22/06/2017"));
        this.instanceOne = new ToDoItem(123L, "test", "this is a test", dueDate, Status.IN_PROGRESS);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testInsertToDoItem() {
        resources.target("/todos").request().post(Entity.entity(this.instanceOne, MediaType.APPLICATION_JSON));
        assertEquals(this.instanceOne, resources.target("/todos/123").request().get(ToDoItem.class));
    }

    @Test
    public void testFindItemByName() {
        resources.target("/todos").request().post(Entity.entity(this.instanceOne, MediaType.APPLICATION_JSON));
        assertEquals(this.instanceOne, resources.target("/todos/find").queryParam("name", "test").request().get(ToDoItem.class));
    }

    @Test(expected= BadRequestException.class)
    public void testFindItemByNameFailsWithoutNameParam() {
        resources.target("/todos").request().post(Entity.entity(this.instanceOne, MediaType.APPLICATION_JSON));
        resources.target("/todos/find").request().get(ToDoItem.class);
    }

    @Test
    public void testInsertAndDeleteItem() {
        resources.target("/todos").request().post(Entity.entity(this.instanceOne, MediaType.APPLICATION_JSON));
        assertEquals(this.instanceOne, resources.target("/todos/123").request().get(ToDoItem.class));
        resources.target("/todos/123").request().delete();
        assertNull(resources.target("/todos/123").request().get(ToDoItem.class));
    }


}