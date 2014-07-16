package org.kiwi.domain;

import org.bson.types.ObjectId;

public class OrderItem {
    private ObjectId productId;
    private int quantity;
    private int price;

    //morphia
    OrderItem() {
    }

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
