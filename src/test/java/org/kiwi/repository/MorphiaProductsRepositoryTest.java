package org.kiwi.repository;

import org.junit.Before;
import org.junit.Test;
import org.kiwi.domain.Product;

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
}
