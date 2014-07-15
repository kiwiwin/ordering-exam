package org.kiwi.resource.representation;

import org.kiwi.domain.Order;
import org.kiwi.domain.User;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
public class OrderRef {

    private User user;
    private Order order;
    private UriInfo uriInfo;

    public OrderRef() {

    }

    public OrderRef(User user, Order order, UriInfo uriInfo) {
        this.user = user;
        this.order = order;
        this.uriInfo = uriInfo;
    }

    @XmlElement
    public String getId() {
        return order.getId().toString();
    }

    @XmlElement
    public String getReceiver() {
        return order.getReceiver();
    }

    @XmlElement
    public String getShippingAddress() {
        return order.getShippingAddress();
    }

    @XmlElement
    public String getUri() {
        return uriInfo.getBaseUri() + "users/" + user.getId() + "/orders/" + order.getId();
    }

    @XmlElement
    public String getCreatedAt() {
        return order.getCreatedAt().toString();
    }

    @XmlElement(name = "orderItems")
    public List<OrderItemRef> getOrderItems() {
        return order.getOrderItems().stream()
                .map(orderItem -> new OrderItemRef(orderItem, uriInfo))
                .collect(Collectors.toList());
    }
}
