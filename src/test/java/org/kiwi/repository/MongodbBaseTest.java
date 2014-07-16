package org.kiwi.repository;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;

public class MongodbBaseTest {
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    protected MongoClient mongo;
    protected DB db;
    private MongodExecutable mongodExecutable;
    private MongodProcess mongod;

    @Before
    public void setUp() throws Exception {
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(12345, Network.localhostIsIPv6())).build());
        mongod = mongodExecutable.start();

        mongo = new MongoClient("localhost", 12345);
        db = mongo.getDB("embedded-mongo");
    }

    @After
    public void tearDown() throws Exception {
        mongod.stop();
        mongodExecutable.stop();
    }
}