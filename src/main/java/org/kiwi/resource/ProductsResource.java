package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Product;
import org.kiwi.resource.repository.ProductsRepository;
import org.kiwi.resource.representation.ProductRef;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("products")
public class ProductsResource {
    @Inject
    private ProductsRepository productsRepository;


    @GET
    @Path("{productId}")
    public ProductRef getProductById(@PathParam("productId") ObjectId productId) {
        final Product product = productsRepository.getProductById(productId);
        return new ProductRef(product);
    }
}
