package org.kiwi.domain;

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

    public String getPaymentType() {
        return paymentType;
    }

    public int getAmount() {
        return amount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
