package org.kiwi.repository;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.kiwi.resource.domain.Product;
import org.kiwi.resource.repository.MongoProductsRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MongoProductsRepositoryTest {
    private MongoProductsRepository productsRepository;
    private Product newProduct;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("test");
        productsRepository = new MongoProductsRepository(db);
        newProduct = productsRepository.createProduct(new Product("apple juice", "good"));
    }

    @Test
    public void should_get_product_by_id() {
        final Product productFromDb = productsRepository.getProductById(newProduct.getId());

        assertThat(productFromDb.getName(), is("apple juice"));
    }
}
