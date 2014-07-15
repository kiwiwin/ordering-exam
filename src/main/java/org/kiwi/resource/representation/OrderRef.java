package org.kiwi.resource.representation;

import org.kiwi.resource.domain.Order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderRef {

    private final Order order;

    public OrderRef(Order order) {
        this.order = order;
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
}