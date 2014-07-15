package org.kiwi.resource.repository;

import com.mongodb.*;
import org.bson.types.ObjectId;
import org.kiwi.domain.Product;

import java.util.ArrayList;
import java.util.List;

import static org.kiwi.domain.ProductWithId.productWithId;

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
                .add("currentPrice", product.getCurrentPrice())
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
        final DBCursor productsCursor = db.getCollection("products").find();

        final ArrayList<Product> products = new ArrayList<>();

        while (productsCursor.hasNext()) {
            products.add(mapProductFromDocument(productsCursor.next()));
        }

        return products;
    }

    private Product mapProductFromDocument(DBObject productDocument) {
        final String name = (String) productDocument.get("name");
        final String description = (String) productDocument.get("description");
        final Object id = productDocument.get("_id");
        final int currentPrice = (int) productDocument.get("currentPrice");

        return productWithId(id.toString(), new Product(name, description, currentPrice));
    }
}
