package com.shop.service;

import com.shop.model.Authority;

import java.util.Optional;

public interface IAuthorityService {
    Optional<Authority>findByName(String name);
}
