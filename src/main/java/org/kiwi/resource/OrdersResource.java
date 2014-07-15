package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Order;
import org.kiwi.resource.domain.User;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.kiwi.resource.representation.OrderRef;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrdersResource {

    private final User user;

    public OrdersResource(User user) {
        this.user = user;
    }

    @GET
    @Path("{orderId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public OrderRef getOrderById(@PathParam("orderId") ObjectId orderId, @Context UriInfo uriInfo) {
        final Order order = user.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException();
        }

        return new OrderRef(user, order, uriInfo);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<OrderRef> getAllOrders(@Context UriInfo uriInfo) {
        return user.getOrders().stream()
                .map(order -> new OrderRef(user, order, uriInfo))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response placeOrder(Map orderParams) {
        return Response.status(201).build();
    }
}
