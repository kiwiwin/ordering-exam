package org.kiwi.resource.domain;

import java.sql.Timestamp;

public class Payment {
    private final String paymentType;
    private final int amount;
    private final Timestamp createdAt;

    public Payment(String paymentType, int amount, Timestamp createdAt) {
        this.paymentType = paymentType;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
