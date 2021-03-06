package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.App;
import org.kiwi.domain.*;
import org.kiwi.repository.ProductsRepository;
import org.kiwi.repository.UsersRepository;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;
import static org.kiwi.domain.OrderWithId.orderWithId;
import static org.kiwi.domain.ProductWithId.productWithId;
import static org.kiwi.domain.UserWithId.userWithId;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrdersResourceTest extends JerseyTest {
    private static final String USER_ID = "53c4971cbaee369cc69d9e2d";
    private static final String PRODUCT_ID = "53c4971cbaee369cc69d9e2f";
    private static final String ORDER_ID = "53c4971cbaee369cc69d9e2e";
    private static final String NEW_ORDER_ID = "53c4971cbaee369cc69d9e2e";
    private static final String NOT_EXIST_USER_ID = "53c4971cbaee369cc69d9e2a";
    private static final String NOT_EXIST_ORDER_ID = "53c4971cbaee369cc69d9e2a";
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProductsRepository productsRepository;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;
    private User user;
    private Order order;
    private Product product;
    private Order createdOrder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        user = userWithId(USER_ID, new User("kiwi"));

        product = productWithId(new ObjectId(PRODUCT_ID), new Product("apple juice", "good", 100));
        order = orderWithId(ORDER_ID, new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), asList(new OrderItem(product, 3, 100))));

        createdOrder = orderWithId(NEW_ORDER_ID, new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), asList(new OrderItem(product, 3, 100))));
        user.placeOrder(order);
    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new ResourceConfig().
                packages("org.kiwi.resource")
                .register(App.createMoxyJsonResolver())
                .register(new MoxyXmlFeature())
                .register(JacksonFeature.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(usersRepository).to(UsersRepository.class);
                        bind(productsRepository).to(ProductsRepository.class);
                    }
                });
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Test
    public void should_get_order_by_id() {
        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);

        final Response response = target("/users/" + USER_ID + "/orders/" + ORDER_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final Map order = response.readEntity(Map.class);

        assertThat(order.get("receiver"), is("Jingcheng Wen"));
        assertThat(order.get("shippingAddress"), is("Sanli,Chengdu"));
        assertThat(order.get("id"), is(ORDER_ID));
        assertThat(order.get("createdAt"), is(new Timestamp(114, 1, 1, 0, 0, 0, 0).toString()));
        assertThat((String) order.get("uri"), endsWith("/users/" + USER_ID + "/orders/" + ORDER_ID));

        final List orderItemsResult = (List) order.get("orderItems");

        assertThat(orderItemsResult.size(), is(1));

        final Map orderItem = (Map) orderItemsResult.get(0);
        assertThat(orderItem.get("quantity"), is(3));
        assertThat(orderItem.get("price"), is(100));

        final Map product = (Map) orderItem.get("product");
        assertThat((String) product.get("uri"), endsWith("/products/" + PRODUCT_ID));
        assertThat(product.get("name"), is("apple juice"));
        assertThat(product.get("description"), is("good"));
    }

    @Test
    public void should_get_order_by_id_with_xml() {
        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);

        final Response response = target("/users/" + USER_ID + "/orders/" + ORDER_ID)
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_get_404_when_user_not_exist() {
        when(usersRepository.getUserById(eq(new ObjectId(NOT_EXIST_USER_ID)))).thenThrow(new ResourceNotFoundException());

        final Response response = target("/users/" + NOT_EXIST_USER_ID + "/orders/" + ORDER_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_get_404_when_order_not_exist() {
        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);

        final Response response = target("/users/" + USER_ID + "/orders/" + NOT_EXIST_ORDER_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_get_all_orders() {
        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);

        final Response response = target("/users/" + USER_ID + "/orders")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final List orders = response.readEntity(List.class);

        assertThat(orders.size(), is(1));

        final Map order = (Map) orders.get(0);

        assertThat(order.get("receiver"), is("Jingcheng Wen"));
        assertThat(order.get("shippingAddress"), is("Sanli,Chengdu"));
        assertThat(order.get("id"), is(ORDER_ID));
        assertThat(order.get("createdAt"), is(new Timestamp(114, 1, 1, 0, 0, 0, 0).toString()));
        assertThat((String) order.get("uri"), endsWith("/users/" + USER_ID + "/orders/" + ORDER_ID));
    }

    @Test
    public void should_get_all_orders_with_xml() {
        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);

        final Response response = target("/users/" + USER_ID + "/orders")
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_create_order() {
        Map newOrder = new HashMap<>();
        newOrder.put("receiver", "Jingcheng Wen");
        newOrder.put("shippingAddress", "Sanli,Chengdu");
        newOrder.put("createdAt", new Timestamp(114, 1, 1, 0, 0, 0, 0).toString());

        List orderItems = new ArrayList<>();
        Map orderItem = new HashMap<>();
        orderItem.put("productId", PRODUCT_ID);
        orderItem.put("quantity", 3);
        orderItem.put("price", 210);
        orderItems.add(orderItem);

        newOrder.put("orderItems", orderItems);

        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);
        when(productsRepository.getProductById(eq(new ObjectId(PRODUCT_ID)))).thenReturn(product);
        when(usersRepository.placeOrder(eq(user), orderArgumentCaptor.capture())).thenReturn(createdOrder);

        final Response response = target("/users/" + USER_ID + "/orders")
                .request()
                .post(Entity.entity(newOrder, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(201));
        assertThat(response.getHeaderString("location"), endsWith("/users/" + USER_ID + "/orders/" + NEW_ORDER_ID));

        assertThat(orderArgumentCaptor.getValue().getReceiver(), is("Jingcheng Wen"));
        assertThat(orderArgumentCaptor.getValue().getShippingAddress(), is("Sanli,Chengdu"));
        assertThat(orderArgumentCaptor.getValue().getCreatedAt(), is(new Timestamp(114, 1, 1, 0, 0, 0, 0)));

        final Product productCaptor = orderArgumentCaptor.getValue().getOrderItems().get(0).getProduct();
        assertThat(productCaptor.getName(), is("apple juice"));
        assertThat(productCaptor.getDescription(), is("good"));
        assertThat(productCaptor.getCurrentPrice(), is(100));
    }


    @Test
    public void should_get_order_payment() {
        order.pay(new Payment("cash", 100, new Timestamp(114, 1, 1, 0, 1, 0, 0)));

        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);

        final Response response = target("/users/" + USER_ID + "/orders/" + ORDER_ID + "/payment")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final Map payment = response.readEntity(Map.class);

        assertThat(payment.get("paymentType"), is("cash"));
        assertThat(payment.get("amount"), is(100));
        assertThat(payment.get("createdAt"), is(new Timestamp(114, 1, 1, 0, 1, 0, 0).toString()));
        assertThat((String) payment.get("uri"), endsWith("/users/" + USER_ID + "/orders/" + ORDER_ID + "/payment"));
    }

    @Test
    public void should_pay_for_order() {
        when(usersRepository.getUserById(eq(new ObjectId(USER_ID)))).thenReturn(user);
        when(usersRepository.payOrder(anyObject(), anyObject(), anyObject())).thenReturn(new Payment("cash", 100, new Timestamp(114, 1, 1, 0, 1, 0, 0)));

        final MultivaluedMap<String, String> paymentValues = new MultivaluedHashMap<>();
        paymentValues.putSingle("paymentType", "cash");
        paymentValues.putSingle("amount", "100");
        paymentValues.putSingle("createdAt", new Timestamp(114, 1, 1, 0, 0, 0, 0).toString());

        final Response response = target("/users/" + USER_ID + "/orders/" + ORDER_ID + "/payment")
                .request()
                .post(Entity.form(paymentValues));

        assertThat(response.getStatus(), is(201));
    }
}
