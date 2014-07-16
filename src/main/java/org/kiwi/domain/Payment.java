package org.kiwi.domain;

import java.sql.Timestamp;

public class Payment {
    private String paymentType;
    private int amount;
    private Timestamp createdAt;

    Payment() {
    }

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
