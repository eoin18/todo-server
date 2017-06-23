package io.emccarthy.todo.api;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class ToDoItemTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private ToDoItem instanceOne;
    private ToDoItem instanceTwo;

    @Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ToDoItem.DATE_FORMAT, Locale.getDefault());
        MAPPER.setDateFormat(simpleDateFormat);
        LocalDate dueDate = LocalDate.fromDateFields(simpleDateFormat.parse("22/06/2017"));
        this.instanceOne = new ToDoItem(123L, "test", "this is a test", dueDate, Status.IN_PROGRESS);
        this.instanceTwo = new ToDoItem(321L, "test2", "this is also a test", dueDate, Status.COMPLETE);
    }

    @Test
    public void testEquals() throws Exception {
        assertNotEquals(this.instanceOne, this.instanceTwo);
        assertEquals(this.instanceOne, this.instanceOne);
        assertEquals(this.instanceTwo, this.instanceTwo);
    }

    @Test
    public void testHashCode() throws Exception {
        assertNotEquals(this.instanceOne.hashCode(), this.instanceTwo.hashCode());
        assertEquals(this.instanceOne.hashCode(), this.instanceOne.hashCode());
        assertEquals(this.instanceTwo.hashCode(), this.instanceTwo.hashCode());
    }

    @Test
    public void testClone() throws Exception {
        ToDoItem clone = (ToDoItem) this.instanceOne.clone();
        assertNotSame(this.instanceOne, clone);
        assertEquals(this.instanceOne, clone);
        assertEquals(this.instanceOne.hashCode(), clone.hashCode());
        assertEquals(this.instanceOne.toString(), clone.toString());
    }

    @Test
    public void serializedToJSON() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(FixtureHelpers.fixture("fixtures/todo.json"), ToDoItem.class));
        assertEquals(expected, MAPPER.writeValueAsString(this.instanceOne));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        ToDoItem actual = MAPPER.readValue(FixtureHelpers.fixture("fixtures/todo.json"), ToDoItem.class);
        assertEquals(this.instanceOne, actual);
    }

}