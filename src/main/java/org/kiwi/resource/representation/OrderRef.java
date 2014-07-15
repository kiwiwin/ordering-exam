package org.kiwi.resource.representation;

import org.kiwi.resource.domain.Order;
import org.kiwi.resource.domain.User;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderRef {

    private final User user;
    private final Order order;
    private final UriInfo uriInfo;

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
}
