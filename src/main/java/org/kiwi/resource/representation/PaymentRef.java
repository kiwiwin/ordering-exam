package org.kiwi.resource.representation;

import org.kiwi.resource.domain.Payment;

import javax.xml.bind.annotation.XmlElement;

public class PaymentRef {
    private final Payment payment;

    public PaymentRef(Payment payment) {
        this.payment = payment;
    }

    @XmlElement
    public String getPaymentType() {
        return payment.getPaymentType();
    }

    @XmlElement
    public int getAmount() {
        return payment.getAmount();
    }

    @XmlElement
    public String getCreatedAt() {
        return payment.getCreatedAt().toString();
    }
}
