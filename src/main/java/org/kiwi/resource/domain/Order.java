package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

import java.sql.Timestamp;

public class Order {
    ObjectId id;
    private final String receiver;
    private String shippingAddress;
    private Timestamp createdAt;

    public Order(String receiver, String shippingAddress, Timestamp createdAt) {
        this.receiver = receiver;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
