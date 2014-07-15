package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Order;
import org.kiwi.resource.domain.OrderItem;
import org.kiwi.resource.domain.User;
import org.kiwi.resource.exception.ResourceNotFoundException;
import org.kiwi.resource.repository.UsersRepository;
import org.kiwi.resource.representation.OrderRef;
import org.kiwi.resource.representation.PaymentRef;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrdersResource {

    private final User user;
    private final UsersRepository usersRepository;

    public OrdersResource(User user, UsersRepository usersRepository) {
        this.user = user;
        this.usersRepository = usersRepository;
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

    @GET
    @Path("{orderId}/payment")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PaymentRef getPayment(@PathParam("orderId") ObjectId orderId, @Context UriInfo uriInfo) {
        final Order order = user.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException();
        }

        return new PaymentRef(order.getPayment(), uriInfo);
    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response placeOrder(Map orderParams, @Context UriInfo uriInfo) {
        final Order order = getOrderFromOrderParams(orderParams);
        final Order newOrder = usersRepository.placeOrder(user, order);
        return Response.status(201).header("location", new OrderRef(user, newOrder, uriInfo).getUri()).build();
    }

    private Order getOrderFromOrderParams(Map orderParams) {
        return new Order((String) orderParams.get("receiver"),
                (String) orderParams.get("shippingAddress"),
                Timestamp.valueOf((String) orderParams.get("createdAt")), getOrderItemsFromParams(orderParams));
    }

    private List<OrderItem> getOrderItemsFromParams(Map orderParams) {
        final List orderItemsParams = (List) orderParams.get("orderItems");

        List<OrderItem> orderItems = new ArrayList<>();

        for (Object orderItemParams : orderItemsParams) {
            orderItems.add(getOrderItemFromParams((Map) orderItemParams));
        }

        return orderItems;
    }

    private OrderItem getOrderItemFromParams(Map orderItemParams) {
        return new OrderItem(new ObjectId((String) orderItemParams.get("productId")),
                (int) orderItemParams.get("quantity"),
                (int) orderItemParams.get("price"));
    }
}
