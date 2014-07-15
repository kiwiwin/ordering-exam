package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.App;
import org.kiwi.resource.domain.Order;
import org.kiwi.resource.domain.OrderItem;
import org.kiwi.resource.domain.Payment;
import org.kiwi.resource.domain.User;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.kiwi.resource.repository.UsersRepository;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;
import static org.kiwi.resource.domain.OrderWithId.orderWithId;
import static org.kiwi.resource.domain.UserWithId.userWithId;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrdersResourceTest extends JerseyTest {
    @Mock
    private UsersRepository usersRepository;


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
                    }
                });
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Test
    public void should_get_order_by_id() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));

        final List<OrderItem> orderItems = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));
        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2e", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), orderItems)));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final Map order = response.readEntity(Map.class);

        assertThat(order.get("receiver"), is("Jingcheng Wen"));
        assertThat(order.get("shippingAddress"), is("Sanli,Chengdu"));
        assertThat(order.get("id"), is("53c4971cbaee369cc69d9e2e"));
        assertThat(order.get("createdAt"), is(new Timestamp(114, 1, 1, 0, 0, 0, 0).toString()));
        assertThat((String) order.get("uri"), endsWith("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e"));

        final List orderItemsResult = (List) order.get("orderItems");

        assertThat(orderItemsResult.size(), is(1));

        final Map orderItem = (Map) orderItemsResult.get(0);
        assertThat((String) orderItem.get("productUri"), endsWith("/products/53c4971cbaee369cc69d9e2f"));
        assertThat(orderItem.get("quantity"), is(3));
        assertThat(orderItem.get("price"), is(100));
    }

    @Test
    public void should_get_order_by_id_with_xml() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));
        final List<OrderItem> orderItems = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));

        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2e", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), orderItems)));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e")
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_get_404_when_user_not_exist() {
        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenThrow(new ResourceNotFoundException());

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_get_404_when_order_not_exist() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_get_all_orders() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));

        final List<OrderItem> orderItems1 = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));
        final List<OrderItem> orderItems2 = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));
        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2e", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), orderItems1)));
        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2f", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 2, 1, 0, 0, 0, 0), orderItems2)));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final List orders = response.readEntity(List.class);

        assertThat(orders.size(), is(2));

        final Map order = (Map) orders.get(0);

        assertThat(order.get("receiver"), is("Jingcheng Wen"));
        assertThat(order.get("shippingAddress"), is("Sanli,Chengdu"));
        assertThat(order.get("id"), is("53c4971cbaee369cc69d9e2e"));
        assertThat(order.get("createdAt"), is(new Timestamp(114, 1, 1, 0, 0, 0, 0).toString()));
        assertThat((String) order.get("uri"), endsWith("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e"));
    }

    @Test
    public void should_get_all_orders_with_xml() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));
        final List<OrderItem> orderItems1 = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));
        final List<OrderItem> orderItems2 = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));
        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2e", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), orderItems1)));
        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2f", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 2, 1, 0, 0, 0, 0), orderItems2)));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders")
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_create_order() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));

        Map newOrder = new HashMap<>();
        newOrder.put("receiver", "Jingcheng Wen");
        newOrder.put("shippingAddress", "Sanli,Chengdu");
        newOrder.put("createdAt", new Timestamp(114, 1, 1, 0, 0, 0, 0).toString());

        List orderItems = new ArrayList<>();
        Map orderItem = new HashMap<>();
        orderItem.put("productId", "53c4971cbaee369cc69d9e2a");
        orderItem.put("quantity", 3);
        orderItem.put("price", 210);
        orderItems.add(orderItem);

        newOrder.put("orderItems", orderItems);

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);
        when(usersRepository.placeOrder(eq(user), anyObject())).thenReturn(orderWithId("53c4971cbaee369cc69d9e2f", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2a"), 3, 100)))));

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders")
                .request()
                .post(Entity.entity(newOrder, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(201));
        assertThat(response.getHeaderString("location"), endsWith("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2f"));
    }



    @Test
    public void should_get_order_payment() {
        final User user = userWithId("53c4971cbaee369cc69d9e2d", new User("kiwi"));

        final List<OrderItem> orderItems = Arrays.asList(new OrderItem(new ObjectId("53c4971cbaee369cc69d9e2f"), 3, 100));

        final Order newOrder = orderWithId("53c4971cbaee369cc69d9e2e", new Order("Jingcheng Wen", "Sanli,Chengdu", new Timestamp(114, 1, 1, 0, 0, 0, 0), orderItems));
        user.placeOrder(newOrder);
        newOrder.pay(new Payment("cash", 100, new Timestamp(114, 1, 1, 0, 1, 0, 0)));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e/payment")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final Map payment = response.readEntity(Map.class);

        assertThat(payment.get("paymentType"), is("cash"));
        assertThat(payment.get("amount"), is(100));
        assertThat(payment.get("createdAt"), is(new Timestamp(114, 1, 1, 0, 1, 0, 0).toString()));
    }

}
