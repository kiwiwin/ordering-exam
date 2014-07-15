package org.kiwi.resource.representation;

import org.kiwi.domain.Product;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductRef {
    private Product product;
    private UriInfo uriInfo;

    public ProductRef() {

    }

    public ProductRef(Product product, UriInfo uriInfo) {
        this.product = product;
        this.uriInfo = uriInfo;
    }

    @XmlElement
    public String getId() {
        return product.getId().toString();
    }

    @XmlElement
    public String getName() {
        return product.getName();
    }

    @XmlElement
    public String getDescription() {
        return product.getDescription();
    }

    @XmlElement
    public String getUri() {
        return uriInfo.getBaseUri() + "products/" + product.getId().toString();
    }

    @XmlElement
    public int getCurrentPrice() {
        return product.getCurrentPrice();
    }
}
