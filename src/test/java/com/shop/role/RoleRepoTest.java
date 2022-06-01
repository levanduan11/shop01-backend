package com.shop.role;

import com.shop.model.Authority;
import com.shop.repository.AuthorityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepoTest {

    @Autowired private AuthorityRepository repository;

    @Test
    public void create(){
        Authority admin=new Authority();
        admin.setName("SALESPERSON");
        admin.setDescription("customers, shipping, orders and sales report");
        Authority admin1=new Authority();
        admin1.setName("EDITOR");
        admin1.setDescription("manage categories, brands,products, articles and menus");


        Authority admin2=new Authority();
        admin2.setName("SHIPPER");
        admin2.setDescription("view products, view orders and update order status");


        Authority admin3=new Authority();
        admin3.setName("ASSISTANT");
        admin3.setDescription("manage questions and reviews");
        repository.saveAll(List.of(admin,admin1,admin2,admin3));
    }

    @Test
    public void find(){
        String name="ADMIN";
        Authority authority=repository.findById(1l).get();
        System.out.println(authority);
    }
}
