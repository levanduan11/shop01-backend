package com.shop.web.rest.client;

import com.shop.repository.BrandRepository;
import com.shop.service.Impl.BrandServiceImpl;
import com.shop.service.dto.BrandDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/b/top")
    public ResponseEntity<List<BrandDTO>>forHomePage(){
        List<BrandDTO>brandDTOS=brandRepository.findFirst12()
                .stream()
                .map(BrandDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(brandDTOS);
    }


}
