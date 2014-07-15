package org.kiwi.domain;

import org.bson.types.ObjectId;

public class OrderWithId {
    private static Order orderWithId(ObjectId id, Order order) {
        order.id = id;
        return order;
    }

    public static Order orderWithId(String id, Order order) {
        return orderWithId(new ObjectId(id), order);
    }
}
