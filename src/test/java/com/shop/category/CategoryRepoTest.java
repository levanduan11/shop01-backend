package com.shop.category;


import com.shop.errors.CategoryNotFoundException;
import com.shop.model.Category;
import com.shop.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepoTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void test() {
        String name = "Laptops";
        String aliasOrName = "laptop_computers";
        Category category = categoryRepository.findByName(name).get();
        System.out.println(category.getName());
       Category category1 = categoryRepository.findByAlias(aliasOrName).orElse(category);

        Category category2 =categoryRepository.findByAlias(aliasOrName)
                .orElse(categoryRepository.findByName(aliasOrName).get());



       // Category category1 = categoryRepository.findByAlias(aliasOrName).get();
                //.orElse(categoryRepository.findByName(aliasOrName).orElseThrow(CategoryNotFoundException::new));

        System.out.println(category1.getName());
        System.out.println(category2.getName());
    }
}
