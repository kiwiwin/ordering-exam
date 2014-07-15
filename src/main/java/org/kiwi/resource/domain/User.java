package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class User {
    ObjectId id;
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
