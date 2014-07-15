package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class ProductWithId {
    public static Product productWithId(ObjectId id, Product product) {
        product.id = id;
        return product;
    }

    public static Product productWithId(String id, Product product) {
        return productWithId(new ObjectId(id), product);
    }
}
