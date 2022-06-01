package com.shop.service;

import com.shop.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User>findByUsername(String username);
    Optional<User>findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);
}
