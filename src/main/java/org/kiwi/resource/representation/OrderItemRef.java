package org.kiwi.resource.representation;

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
    public String getProductUri() {
        return uriInfo.getBaseUri() + "products/" + orderItem.getProductId();
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
