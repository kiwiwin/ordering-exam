package org.kiwi;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.kiwi.resource.repository.MongoProductsRepository;
import org.kiwi.resource.repository.MongoUsersRepository;
import org.kiwi.resource.repository.ProductsRepository;
import org.kiwi.resource.repository.UsersRepository;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpServer httpServer = startServer();

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                httpServer.shutdownNow();
            }
        }
    }

    private static HttpServer startServer() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("production");

        final MongoUsersRepository mongoUserRepository = new MongoUsersRepository(db);
        final MongoProductsRepository mongoProductRepository = new MongoProductsRepository(db);

        final ResourceConfig config = new ResourceConfig()
                .packages("org.kiwi.resource")
                .register(App.createMoxyJsonResolver())
                .register(new MoxyXmlFeature())
                .register(JacksonFeature.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(mongoUserRepository).to(UsersRepository.class);
                        bind(mongoProductRepository).to(ProductsRepository.class);
                    }
                });
        return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), config);
    }

    public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}
