package org.kiwi.domain;

import jdk.nashorn.internal.ir.annotations.Reference;

public class OrderItem {
    @Reference
    private Product product;
    private int quantity;
    private int price;

    //morphia
    OrderItem() {
    }

    public OrderItem(Product product, int quantity, int price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
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
