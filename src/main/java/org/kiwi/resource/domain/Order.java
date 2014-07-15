package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class Order {
    ObjectId id;
    private final String receiver;

    public Order(String receiver) {
        this.receiver = receiver;
    }

    public ObjectId getId() {
        return id;
    }

    public String getReceiver() {
        return receiver;
    }
}
