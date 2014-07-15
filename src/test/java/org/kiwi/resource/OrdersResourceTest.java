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
import org.kiwi.resource.domain.User;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.kiwi.resource.repository.UsersRepository;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.kiwi.resource.domain.OrderWithId.orderWithId;
import static org.kiwi.resource.domain.UserWithId.userWithId;
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
        user.placeOrder(orderWithId("53c4971cbaee369cc69d9e2e", new Order("Jingcheng Wen")));

        when(usersRepository.getUserById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(user);

        final Response response = target("/users/53c4971cbaee369cc69d9e2d/orders/53c4971cbaee369cc69d9e2e")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final Map order = response.readEntity(Map.class);

        assertThat(order.get("receiver"), is("Jingcheng Wen"));
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
}
