package io.emccarthy.todo.health;

import static org.junit.Assert.assertTrue;

import com.codahale.metrics.health.HealthCheck;
import io.emccarthy.todo.AbstractMongoDBTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseHealthCheckTest extends AbstractMongoDBTest{

    private DatabaseHealthCheck healthCheck;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.healthCheck = new DatabaseHealthCheck(mongoDatabase);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCheckCorrectWhenDBExists() throws Exception {
        HealthCheck.Result check = this.healthCheck.check();
        assertTrue(check.isHealthy());
    }

}