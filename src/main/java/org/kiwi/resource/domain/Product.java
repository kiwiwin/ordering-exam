package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class Product {
    ObjectId id;
    private final String name;
    private final String description;
    private final int currentPrice;

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
