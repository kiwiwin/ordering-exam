package org.kiwi.repository;

import org.junit.Before;
import org.junit.Test;
import org.kiwi.domain.*;

import java.sql.Timestamp;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MorphiaUsersRepositoryTest extends MorphiaBaseTest {

    private MorphiaUsersRepository usersRepository;
    private MorphiaProductsRepository productsRepository;
    private Product product;
    private User user;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        usersRepository = new MorphiaUsersRepository(datastore);
        productsRepository = new MorphiaProductsRepository(datastore);

        product = productsRepository.createProduct(new Product("apple juice", "good", 100));
        user = usersRepository.createUser(new User("kiwi"));
    }

    @Test
    public void should_get_user_by_id() {
        final User userFromDb = usersRepository.getUserById(user.getId());

        assertThat(userFromDb.getName(), is("kiwi"));
    }

    @Test
    public void should_user_place_order() {
        final Order order = usersRepository.placeOrder(user, new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), asList(new OrderItem(product.getId(), 3, 100))));

        final User userFromDb = usersRepository.getUserById(user.getId());

        final Order orderFromDb = userFromDb.getOrderById(order.getId());

        assertThat(orderFromDb.getReceiver(), is("Jingcheng Wen"));
        assertThat(orderFromDb.getShippingAddress(), is("Sanli,Chengdu"));
        assertThat(orderFromDb.getCreatedAt(), is(new Timestamp(114, 1, 1, 0, 0, 0, 0)));

        assertThat(orderFromDb.getOrderItems().size(), is(1));

        final OrderItem orderItem = orderFromDb.getOrderItems().get(0);

        assertThat(orderItem.getProductId(), is(product.getId()));
        assertThat(orderItem.getQuantity(), is(3));
        assertThat(orderItem.getPrice(), is(100));
    }

    @Test
    public void should_pay_order() {
        final Order order = usersRepository.placeOrder(user, new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), asList(new OrderItem(product.getId(), 3, 100))));

        usersRepository.payOrder(user, order, new Payment("cash", 100, new Timestamp(114, 1, 1, 1, 0, 0, 0)));

        final User userFromDb = usersRepository.getUserById(user.getId());

        final Order orderFromDb = userFromDb.getOrderById(order.getId());

        final Payment paymentFromDb = orderFromDb.getPayment();

        assertThat(paymentFromDb.getPaymentType(), is("cash"));
        assertThat(paymentFromDb.getAmount(), is(100));
        assertThat(paymentFromDb.getCreatedAt(), is(new Timestamp(114, 1, 1, 1, 0, 0, 0)));
    }
}
