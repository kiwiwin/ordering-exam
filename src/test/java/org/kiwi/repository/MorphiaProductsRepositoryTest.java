package org.kiwi.repository;

import org.junit.Before;
import org.junit.Test;
import org.kiwi.domain.Product;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MorphiaProductsRepositoryTest extends MorphiaBaseTest {

    private MorphiaProductsRepository productsRepository;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        productsRepository = new MorphiaProductsRepository(datastore);
    }

    @Test
    public void should_get_product_by_id() {
        final Product product = productsRepository.createProduct(new Product("apple juice", "good", 100));

        final Product productFromDb = productsRepository.getProductById(product.getId());

        assertThat(productFromDb.getName(), is("apple juice"));
        assertThat(productFromDb.getDescription(), is("good"));
        assertThat(productFromDb.getCurrentPrice(), is(100));
    }

    @Test
    public void should_get_all_products() {
        productsRepository.createProduct(new Product("apple juice", "good", 100));
        productsRepository.createProduct(new Product("banana juice", "bad", 200));

        final List<Product> allProducts = productsRepository.getAllProducts();

        assertThat(allProducts.size(), is(2));

        final Product appleProduct = allProducts.get(0);
        assertThat(appleProduct.getName(), is("apple juice"));
        assertThat(appleProduct.getDescription(), is("good"));
        assertThat(appleProduct.getCurrentPrice(), is(100));

        final Product bananaProduct = allProducts.get(1);
        assertThat(bananaProduct.getName(), is("banana juice"));
        assertThat(bananaProduct.getDescription(), is("bad"));
        assertThat(bananaProduct.getCurrentPrice(), is(200));
    }
}
