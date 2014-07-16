package org.kiwi.representation;

import org.kiwi.domain.OrderItem;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;

public class OrderItemRef {
    private final OrderItem orderItem;
    private final UriInfo uriInfo;

    public OrderItemRef(OrderItem orderItem, UriInfo uriInfo) {
        this.orderItem = orderItem;
        this.uriInfo = uriInfo;
    }

    @XmlElement
    public ProductRef getProduct() {
        return new ProductRef(orderItem.getProduct(), uriInfo);
    }

    @XmlElement
    public int getQuantity() {
        return orderItem.getQuantity();
    }

    @XmlElement
    public int getPrice() {
        return orderItem.getPrice();
    }
}
