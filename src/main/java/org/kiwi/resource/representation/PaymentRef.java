package org.kiwi.resource.representation;

import org.kiwi.domain.Payment;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;

public class PaymentRef {
    private final Payment payment;
    private UriInfo uriInfo;

    public PaymentRef(Payment payment, UriInfo uriInfo) {
        this.payment = payment;
        this.uriInfo = uriInfo;
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

    @XmlElement
    public String getUri() {
        return uriInfo.getAbsolutePath().toString();
    }
}
