package org.kiwi.domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("products")
public class Product {
    @Id
    ObjectId id;
    private String name;
    private String description;
    private int currentPrice;

    Product() {
    }

    public Product(String name, String description, int currentPrice) {
        this.name = name;
        this.description = description;
        this.currentPrice = currentPrice;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }
}
