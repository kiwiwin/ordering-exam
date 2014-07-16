package org.kiwi.domain;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.bson.types.ObjectId;

public class OrderItem {
    private ObjectId productId;
    private int quantity;
    private int price;
    @Reference
    private Product product;

    //morphia
    OrderItem() {
    }

    public OrderItem(ObjectId productId, int quantity, int price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem(Product product, int quantity, int price) {
        this.product = product;
        this.productId = product.getId();
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

    public Product getProduct() {
        return product;
    }
}
