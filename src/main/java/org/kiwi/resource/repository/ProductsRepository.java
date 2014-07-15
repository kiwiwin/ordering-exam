package org.kiwi.resource.repository;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Product;

public interface ProductsRepository {
    Product getProductById(ObjectId productId);
}
