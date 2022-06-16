package com.shop.product;

import com.shop.model.Brand;
import com.shop.model.Category;
import com.shop.model.product.Product;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepoTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void create() {
        Brand brand = entityManager.find(Brand.class, 5l);
        Category category = entityManager.find(Category.class, 5l);

        Product product = new Product();
        product.setName("eee");
        product.setAlias("eee");
        product.setShortDescription("good");
        product.setFullDescription("very good");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(99);
        product.setUpdatedTime(Instant.now());

        Product saved = productRepository.save(product);
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void addExtraImage(){
        Product product=productRepository.findById(1l).get();
        product.addExtraImage("extra5.png");
        product.addExtraImage("extra99.png");
        productRepository.save(product);
    }

    @Test
    public void addDetail(){
        Product product=productRepository.findById(1l).get();
        product.addDetail("detail1","tail1");
        product.addDetail("detail1","tail1");
        productRepository.save(product);
    }

}
