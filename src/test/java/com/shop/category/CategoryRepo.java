package com.shop.category;

import com.shop.model.Category;
import com.shop.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepo {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityManager entityManager;
    List<String>list=new ArrayList<>();

    @Test
    public void create() {
   List<Category>categories=categoryRepository.findAllByParentIdIsNull();

      categories.forEach(x->{
          helper(x);
          updateStack(x);
      });
    }

    @Test
    public void updateParent(){
        Category p=entityManager.find(Category.class,1l);
        Category p1=entityManager.find(Category.class,3l);
        Category category=new Category();
        category.setId(1l);
        p1.setParent(category);
        categoryRepository.save(p1);
        System.out.println(p1);

    }

    @Test
    public void fetch(){
        Optional<Category>category=categoryRepository.findByName("Electronics");
        System.out.println(category.get());
    }

    private void updateRecursion(Category root) {
        if (root == null) {
            return;
        }
        if (root.getChildren().size() > 0) {
            Set<Category> set = root.getChildren();
            for (Category ca : set) {
                helper(ca);
                updateRecursion(ca);
            }
        }
    }

    private void updateStack(Category root){
        Stack<Category>categories=new Stack<>();
        categories.push(root);
        while (!categories.isEmpty()){
            Category ca=categories.pop();
            helper(ca);
            Set<Category>child=ca.getChildren();
            if (child.size()>0){
                child.forEach(c->{
                    categories.push(c);
                });
            }

        }
    }

    private void helper(Category c){
        Category p=c.getParent();
        if (p!=null){
            String idAsString=String.valueOf(p.getId());
            String pPParent=p.getAllParentId();
            String allPP= pPParent!=null? pPParent:"-";
            allPP=allPP.concat(idAsString).concat("-");
            System.out.println(allPP+"->"+c.getName());
            c.setAllParentId(allPP);
        }else {
            c.setAllParentId(null);
        }

    }


}
