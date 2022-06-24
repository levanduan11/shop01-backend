package com.shop.web.rest.client;

import com.shop.repository.BrandRepository;
import com.shop.service.Impl.BrandServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(value = "*")
public class PublicBrandResource {
    private final BrandServiceImpl brandService;
    private final BrandRepository brandRepository;

    public PublicBrandResource(BrandServiceImpl brandService, BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }


}
