package org.kiwi.domain;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    ObjectId id;
    private String name;
    @Reference
    private List<Order> orders = new ArrayList<>();

    //for morphia
    User() {
    }

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
