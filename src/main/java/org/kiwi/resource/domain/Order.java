package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class Order {
    ObjectId id;
    private final String receiver;
    private String shippingAddress;

    public Order(String receiver, String shippingAddress) {
        this.receiver = receiver;
        this.shippingAddress = shippingAddress;
    }

    public ObjectId getId() {
        return id;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
}
