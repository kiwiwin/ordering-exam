package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Order;
import org.kiwi.resource.domain.User;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.kiwi.resource.representation.OrderRef;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class OrdersResource {

    private final User user;

    public OrdersResource(User user) {
        this.user = user;
    }

    @GET
    @Path("{orderId}")
    public OrderRef getOrderById(@PathParam("orderId") ObjectId orderId) {
        final Order order = user.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException();
        }

        return new OrderRef(order);
    }
}
