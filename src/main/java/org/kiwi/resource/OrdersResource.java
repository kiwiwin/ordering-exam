package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.domain.*;
import org.kiwi.repository.ProductsRepository;
import org.kiwi.repository.UsersRepository;
import org.kiwi.representation.OrderRef;
import org.kiwi.representation.PaymentRef;
import org.kiwi.resource.exception.ResourceNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrdersResource {

    private final User user;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;

    public OrdersResource(User user, UsersRepository usersRepository, ProductsRepository productsRepository) {
        this.user = user;
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
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
    @Path("{orderId}/payment")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response payOrder(@PathParam("orderId") ObjectId orderId, Form form, @Context UriInfo uriInfo) {
        final Order order = user.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException();
        }

        final Payment payment = usersRepository.payOrder(user, order, getPaymentFromForm(form));

        return Response.status(201).header("location", new PaymentRef(payment, uriInfo)).build();
    }

    private Payment getPaymentFromForm(Form form) {
        final MultivaluedMap<String, String> map = form.asMap();
        return new Payment(map.getFirst("paymentType"),
                Integer.valueOf(map.getFirst("amount")),
                Timestamp.valueOf(map.getFirst("createdAt")));
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
        final ObjectId productId = new ObjectId((String) orderItemParams.get("productId"));
        final Product product = productsRepository.getProductById(productId);
        return new OrderItem(product,
                (int) orderItemParams.get("quantity"),
                (int) orderItemParams.get("price"));
    }
}
