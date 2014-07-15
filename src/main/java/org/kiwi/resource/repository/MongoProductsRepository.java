package org.kiwi.resource.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Product;

import java.util.List;

import static org.kiwi.resource.domain.ProductWithId.productWithId;

public class MongoProductsRepository implements ProductsRepository {
    private final DB db;

    public MongoProductsRepository(DB db) {
        this.db = db;
    }

    @Override
    public Product createProduct(Product product) {
        final DBObject productDocument = new BasicDBObjectBuilder()
                .add("name", product.getName())
                .add("description", product.getDescription())
                .get();

        db.getCollection("products").insert(productDocument);

        return productWithId(productDocument.get("_id").toString(), product);
    }

    @Override
    public Product getProductById(ObjectId productId) {
        final DBObject productDocument = db.getCollection("products").findOne(new BasicDBObject("_id", productId));

        return mapProductFromDocument(productDocument);
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    private Product mapProductFromDocument(DBObject productDocument) {
        final String name = (String) productDocument.get("name");
        final String description = (String) productDocument.get("description");
        final Object id = productDocument.get("_id");

        return productWithId(id.toString(), new Product(name, description, 100));
    }
}
