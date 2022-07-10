package com.shop.product;

import com.shop.model.product.Product;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepoTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void alias(){
        String alias="sony-zv-1";
      //  Optional<Product> product=productRepository.findByAlias(alias);
        //System.out.println(product.get().getName());
        System.out.println(productRepository.findById(3l));
    }
    @Test
    public void search(){
        String alias="3000 15.6-inch";
     Pageable pageable= PageRequest.of(1,10);
     Page<Product>products=productRepository.search(alias,pageable);
     List<Product>products1=products.getContent();
     assertThat(products1.size()).isEqualTo(3);
    }
    @Test
    public void newProduct(){
      List<Product>list=productRepository.findTop12OrderByCreatedTimeDesc();
        list.forEach(x-> System.out.println(x.getName()));
    }

    @Test
    public void byBrand(){
        Pageable pageable=PageRequest.of(0,3);
      Page<Product>page=productRepository.findAllByBrandId(1l,pageable);
      List<Product>products=page.getContent();
      products.forEach(x-> System.out.println(x));
    }

}
