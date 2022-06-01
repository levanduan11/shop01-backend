package com.shop.web.rest;

import com.shop.service.Impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PublicUserResource {

    private final Logger log = LoggerFactory.getLogger(PublicUserResource.class);
    private final UserServiceImpl userService;

    public PublicUserResource(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/roles")
    public List<String>getAuthorities(){
        return userService.getRoleNames();
    }
}
