package org.kiwi.repository;

import org.bson.types.ObjectId;
import org.kiwi.domain.Product;

import java.util.List;

public interface ProductsRepository {
    Product createProduct(Product product);

    Product getProductById(ObjectId productId);

    List<Product> getAllProducts();
}
