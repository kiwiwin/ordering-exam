package org.kiwi.resource.representation;

import org.kiwi.resource.domain.Product;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductRef {
    private final Product product;

    public ProductRef(Product product) {
        this.product = product;
    }

    @XmlElement
    public String getName() {
        return product.getName();
    }
}
