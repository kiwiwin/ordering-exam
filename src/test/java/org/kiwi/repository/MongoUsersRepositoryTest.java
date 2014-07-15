package org.kiwi.repository;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kiwi.resource.domain.Product;
import org.kiwi.resource.domain.User;
import org.kiwi.resource.repository.MongoProductsRepository;
import org.kiwi.resource.repository.MongoUsersRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MongoUsersRepositoryTest {
    private MongoUsersRepository usersRepository;
    private Product newProduct;
    private DB db;
    private User user;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("test");
        usersRepository = new MongoUsersRepository(db);
        user = usersRepository.createUser(new User("kiwi"));
    }

    @After
    public void tearDown() throws Exception {
        db.dropDatabase();
    }

    @Test
    public void should_get_user_by_id() {
        final User userFromDb = usersRepository.getUserById(user.getId());

        assertThat(userFromDb.getName(), is("kiwi"));
    }
}
