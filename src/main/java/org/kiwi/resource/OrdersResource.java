package org.kiwi.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class OrdersResource {

    @GET
    @Path("{orderId}")
    public String getOrderById() {
        return "";
    }
}
