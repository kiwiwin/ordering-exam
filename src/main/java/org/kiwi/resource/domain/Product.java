package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class Product {
    ObjectId id;
    private final String name;

    public Product(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
