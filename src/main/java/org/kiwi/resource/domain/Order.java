package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    ObjectId id;
    private final String receiver;
    private String shippingAddress;
    private Timestamp createdAt;
    private final List<OrderItem> orderItems;
    private Payment payment;

    public Order(String receiver, String shippingAddress, Timestamp createdAt, List<OrderItem> orderItems) {
        this.receiver = receiver;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void pay(Payment payment) {
        this.payment = payment;
    }
}
