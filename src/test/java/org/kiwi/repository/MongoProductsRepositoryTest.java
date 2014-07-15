package org.kiwi.repository;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kiwi.domain.Product;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MongoProductsRepositoryTest {
    private MongoProductsRepository productsRepository;
    private Product newProduct;
    private DB db;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("test");
        productsRepository = new MongoProductsRepository(db);
        newProduct = productsRepository.createProduct(new Product("apple juice", "good", 100));
        productsRepository.createProduct(new Product("banana juice", "bad", 200));
    }

    @After
    public void tearDown() throws Exception {
        db.dropDatabase();
    }

    @Test
    public void should_get_product_by_id() {
        final Product productFromDb = productsRepository.getProductById(newProduct.getId());

        assertThat(productFromDb.getName(), is("apple juice"));
        assertThat(productFromDb.getDescription(), is("good"));
        assertThat(productFromDb.getCurrentPrice(), is(100));
    }

    @Test
    public void should_get_all_products() {
        final List<Product> productsFromDb = productsRepository.getAllProducts();

        assertThat(productsFromDb.size(), is(2));

        final Product productFromDb = productsFromDb.get(0);

        assertThat(productFromDb.getName(), is("apple juice"));
        assertThat(productFromDb.getDescription(), is("good"));
        assertThat(productFromDb.getCurrentPrice(), is(100));

        final Product productFromDb2 = productsFromDb.get(1);

        assertThat(productFromDb2.getName(), is("banana juice"));
        assertThat(productFromDb2.getDescription(), is("bad"));
        assertThat(productFromDb2.getCurrentPrice(), is(200));
    }
}
