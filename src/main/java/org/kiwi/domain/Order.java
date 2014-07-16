package org.kiwi.domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.sql.Timestamp;
import java.util.List;

@Entity("orders")
public class Order {
    @Id
    ObjectId id;
    private String receiver;
    private String shippingAddress;
    private Timestamp createdAt;
    private List<OrderItem> orderItems;
    private Payment payment;

    Order() {
    }

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

    public Payment getPayment() {
        return payment;
    }
}
