package com.shop.role;

import com.shop.model.Authority;
import com.shop.model.User;
import com.shop.repository.AuthorityRepository;
import com.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepoTest {

    @Autowired private UserRepository repository;
    @Autowired private TestEntityManager manager;
    @Autowired private AuthorityRepository authorityRepository;

    @Test
    public void create(){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String pass=passwordEncoder.encode("1234567");
        User user=new User();
        user.setUsername("le.van.duan");
        user.setPassword(pass);
        user.setEmail("lvduan@gmail.com");
        user.setFirstName("le van");
        user.setLastName("duan");
        user.setActivated(true);
        user.setImageUrl("default.png");
        Long id=1l;
        Authority authority=authorityRepository.findByName("admin").get();
        user.setAuthorities(new HashSet<>(List.of(authority)));
        repository.save(user);
    }
}
