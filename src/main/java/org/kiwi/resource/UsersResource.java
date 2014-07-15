package org.kiwi.resource;

import javax.ws.rs.Path;

@Path("users")
public class UsersResource {
    @Path("{userId}/orders")
    public OrdersResource getOrdersResource() {
        return new OrdersResource();
    }
}
