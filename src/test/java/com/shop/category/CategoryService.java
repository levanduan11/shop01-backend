package com.shop.category;

import com.shop.model.Category;
import com.shop.service.Impl.CategoryServiceImpl;
import com.shop.service.dto.CategoryManger;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@SpringBootTest
public class CategoryService {

    @Autowired
    private CategoryServiceImpl service;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CategoryManger manger;

    @Test
    public void listffd(){
        Category c=service.findOne(1l).get();
      //  manger.insert(c);
        System.out.println(c);
    }

    @Test
    public void list() {
        List<Category> list = service.findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getParent() == null) {
                System.out.println(list.get(i));
                print(list.get(i), "+");
            }
        }
    }

    private void print(Category root, String name) {
        if (root == null) {
            return;
        }
        if (root.getChildren().size() > 0) {
            for (Category c : root.getChildren()) {
                System.out.println(name.concat(c.getName()));
                print(c, name + "+");

            }
        }
    }



}
