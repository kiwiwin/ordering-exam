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
import org.kiwi.domain.Product;
import org.kiwi.repository.ProductsRepository;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;
import static org.kiwi.domain.ProductWithId.productWithId;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductsResourceTest extends JerseyTest {
    @Mock
    private ProductsRepository productsRepository;

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
                        bind(productsRepository).to(ProductsRepository.class);
                    }
                });
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Test
    public void should_get_product_by_id() {
        when(productsRepository.getProductById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(productWithId("53c4971cbaee369cc69d9e2d", new Product("apple juice", "good", 100)));

        final Response response = target("/products/53c4971cbaee369cc69d9e2d")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final Map product = response.readEntity(Map.class);

        assertThat(product.get("name"), is("apple juice"));
        assertThat(product.get("description"), is("good"));
        assertThat((String) product.get("uri"), endsWith("/products/53c4971cbaee369cc69d9e2d"));
        assertThat(product.get("id"), is("53c4971cbaee369cc69d9e2d"));
        assertThat(product.get("currentPrice"), is(100));
    }

    @Test
    public void should_get_product_by_id_with_xml() {
        when(productsRepository.getProductById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenReturn(productWithId("53c4971cbaee369cc69d9e2d", new Product("apple juice", "good", 100)));

        final Response response = target("/products/53c4971cbaee369cc69d9e2d")
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void should_get_404_when_product_not_exist() {
        when(productsRepository.getProductById(eq(new ObjectId("53c4971cbaee369cc69d9e2d")))).thenThrow(new ResourceNotFoundException());

        final Response response = target("/products/53c4971cbaee369cc69d9e2d")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_get_all_products() {
        when(productsRepository.getAllProducts()).thenReturn(Arrays.asList(productWithId("53c4971cbaee369cc69d9e2d", new Product("apple juice", "good", 100))));

        final Response response = target("/products")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));

        final List products = response.readEntity(List.class);

        assertThat(products.size(), is(1));

        final Map product = (Map) products.get(0);

        assertThat(product.get("name"), is("apple juice"));
        assertThat(product.get("description"), is("good"));
        assertThat((String) product.get("uri"), endsWith("/products/53c4971cbaee369cc69d9e2d"));
        assertThat(product.get("id"), is("53c4971cbaee369cc69d9e2d"));
        assertThat(product.get("currentPrice"), is(100));
    }

    @Test
    public void should_get_all_products_with_xml() {
        when(productsRepository.getAllProducts()).thenReturn(Arrays.asList(productWithId("53c4971cbaee369cc69d9e2d", new Product("apple juice", "good", 100))));

        final Response response = target("/products")
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response.getStatus(), is(200));
    }
}
