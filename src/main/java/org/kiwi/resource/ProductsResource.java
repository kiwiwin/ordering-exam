package org.kiwi.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("products")
public class ProductsResource {

    @GET
    @Path("{productId}")
    public String getProductById() {
        return "";
    }
}
