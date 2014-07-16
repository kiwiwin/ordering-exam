package org.kiwi.repository;

import org.bson.types.ObjectId;
import org.kiwi.domain.Product;
import org.mongodb.morphia.Datastore;

import java.util.List;

public class MorphiaProductsRepository implements ProductsRepository {
    private final Datastore datastore;

    public MorphiaProductsRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public Product createProduct(Product product) {
        datastore.save(product);
        return product;
    }

    @Override
    public Product getProductById(ObjectId productId) {
        return datastore.get(Product.class, productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }
}
