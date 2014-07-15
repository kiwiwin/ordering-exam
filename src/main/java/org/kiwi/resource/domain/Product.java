package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class Product {
    ObjectId id;
    private final String name;
    private final String description;

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
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
}
