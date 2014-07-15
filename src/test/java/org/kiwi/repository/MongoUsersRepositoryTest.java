package org.kiwi.repository;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kiwi.domain.*;

import java.sql.Timestamp;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MongoUsersRepositoryTest {
    private MongoUsersRepository usersRepository;
    private MongoProductsRepository productRepository;
    private DB db;
    private User user;
    private Order order;
    private Product product;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("test");

        productRepository = new MongoProductsRepository(db);
        product = productRepository.createProduct(new Product("apple juice", "good", 100));

        usersRepository = new MongoUsersRepository(db);
        user = usersRepository.createUser(new User("kiwi"));

        order = usersRepository.placeOrder(user, new Order("kiwi", "chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), Arrays.asList(new OrderItem(product.getId(), 3, 100))));
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

    @Test
    public void should_get_order() {
        final User userFromDb = usersRepository.getUserById(user.getId());

        final Order orderFromDb = userFromDb.getOrderById(order.getId());

        assertThat(orderFromDb.getReceiver(), is("kiwi"));
        assertThat(orderFromDb.getShippingAddress(), is("chengdu"));
        assertThat(orderFromDb.getOrderItems().size(), is(1));
    }

    @Test
    public void should_pay_order() {
        usersRepository.payOrder(user, order, new Payment("cash", 100, new Timestamp(114, 1, 1, 0, 1, 0, 0)));

        final User userFromDb = usersRepository.getUserById(user.getId());

        final Order orderFromDb = userFromDb.getOrderById(order.getId());

        final Payment paymentFromDb = orderFromDb.getPayment();

        assertThat(paymentFromDb.getPaymentType(), is("cash"));
        assertThat(paymentFromDb.getAmount(), is(100));
        assertThat(paymentFromDb.getCreatedAt().toString(), is(new Timestamp(114, 1, 1, 0, 1, 0, 0).toString()));
    }
}
