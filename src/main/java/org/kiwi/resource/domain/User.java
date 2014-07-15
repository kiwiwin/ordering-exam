package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User {
    ObjectId id;
    private final String name;
    private List<Order> orders = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void placeOrder(Order order) {
        orders.add(order);
    }

    public Order getOrderById(ObjectId orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst().orElseGet(() -> null);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
