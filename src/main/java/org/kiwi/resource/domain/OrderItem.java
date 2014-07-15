package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class OrderItem {
    private final ObjectId productId;
    private final int quantity;
    private final int price;

    public OrderItem(ObjectId productId, int quantity, int price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public ObjectId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
