package org.kiwi.resource.representation;

import org.kiwi.resource.domain.Product;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductRef {
    private final Product product;
    private final UriInfo uriInfo;

    public ProductRef(Product product, UriInfo uriInfo) {
        this.product = product;
        this.uriInfo = uriInfo;
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
}
