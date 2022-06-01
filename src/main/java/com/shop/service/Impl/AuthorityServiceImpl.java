package com.shop.service.Impl;

import com.shop.model.Authority;
import com.shop.repository.AuthorityRepository;
import com.shop.service.IAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class AuthorityServiceImpl implements IAuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Optional<Authority> findByName(String name) {
        return authorityRepository.findByName(name);
    }
}
