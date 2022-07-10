package com.shop.brand;

import com.shop.model.Brand;
import com.shop.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepoTest {

    @Autowired
    BrandRepository brandRepository;

    @Test
    public void testL(){
        List<Brand>brands=brandRepository.findFirst12();
        brands.forEach(x-> System.out.println(x.getName()));
    }
}
