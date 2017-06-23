package io.emccarthy.todo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractMongoDBTest {

    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongodProcess;
    protected static MongoDatabase mongoDatabase;

    @BeforeClass
    public static void setUpClass() throws Exception{
        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                .build());
        mongodProcess = mongodExecutable.start();
        MongoClient client = new MongoClient("localhost", 12345);
        mongoDatabase = client.getDatabase("test");

    }

    @Before
    public void setUp() throws Exception {
        MongoClient client = new MongoClient("localhost", 12345);
        mongoDatabase = client.getDatabase("test");
    }

    @After
    public void tearDown() throws Exception {
        mongoDatabase.drop();
    }

    @AfterClass
    public static void tearDownClass() {
        mongodProcess.stop();
        mongodExecutable.stop();
    }



}
