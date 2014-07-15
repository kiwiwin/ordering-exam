package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.domain.Product;
import org.kiwi.resource.repository.ProductsRepository;
import org.kiwi.resource.representation.ProductRef;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("products")
public class ProductsResource {
    @Inject
    private ProductsRepository productsRepository;


    @GET
    @Path("{productId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ProductRef getProductById(@PathParam("productId") ObjectId productId, @Context UriInfo uriInfo) {
        final Product product = productsRepository.getProductById(productId);
        return new ProductRef(product, uriInfo);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ProductRef> getAllProducts(@Context UriInfo uriInfo) {
        return productsRepository.getAllProducts().stream()
                .map(product -> new ProductRef(product, uriInfo))
                .collect(Collectors.toList());
    }
}
