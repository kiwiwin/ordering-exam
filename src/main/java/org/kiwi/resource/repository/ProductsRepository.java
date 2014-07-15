package org.kiwi.resource.repository;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Product;

import java.util.List;

public interface ProductsRepository {
    Product getProductById(ObjectId productId);

    List<Product> getAllProducts();
}
