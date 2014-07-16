package org.kiwi.repository;

import org.junit.Before;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaBaseTest extends MongodbBaseTest {
    protected Datastore datastore;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        datastore = new Morphia().createDatastore(mongo, db.getName());
    }
}