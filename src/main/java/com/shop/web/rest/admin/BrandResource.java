package com.shop.web.rest.admin;

import com.shop.model.Brand;
import com.shop.model.Category;
import com.shop.repository.BrandRepository;
import com.shop.security.AuthoritiesConstants;
import com.shop.service.Impl.BrandServiceImpl;
import com.shop.service.dto.BrandDTO;
import com.shop.share.ResponseUtil;
import com.shop.errors.BadRequestAlertException;
import com.shop.errors.BrandNameAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api/admin")
public class BrandResource {

    private final static Logger log = LoggerFactory.getLogger(BrandResource.class);

    private final BrandRepository brandRepository;
    private final BrandServiceImpl brandService;

    public BrandResource(BrandRepository brandRepository, BrandServiceImpl brandService) {
        this.brandRepository = brandRepository;
        this.brandService = brandService;
    }

    @PostMapping("/brands")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Brand> createBrand(@Valid @RequestBody BrandDTO brandDTO) throws URISyntaxException {
        log.debug("create brand request {} ", brandDTO);
        if (brandDTO.getId() != null) {
            throw new BadRequestAlertException("A new brand cannot already have an ID");
        }
        if (brandRepository.existsByName(brandDTO.getName())) {
            throw new BrandNameAlreadyUsedException();
        }
        Brand brand = brandService.create(brandDTO);
        return new ResponseEntity<>(brand, HttpStatus.CREATED);

    }

    @PutMapping("/brands/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable(required = false, value = "id") final Long id, @RequestBody BrandDTO brandDTO) {
        log.debug("update category request {} ", brandDTO);

        if (brandDTO.getId() == null || !Objects.equals(id, brandDTO.getId())) {
            throw new BadRequestAlertException("invalid id");
        }

        if (!brandRepository.existsById(id)) {
            throw new BadRequestAlertException("not found category");
        }
        Optional<Brand> existsName = brandRepository.findByName(brandDTO.getName());
        if (existsName.isPresent() && !checkId(existsName, brandDTO)) {
            throw new BrandNameAlreadyUsedException();
        }
        Optional<BrandDTO> optionalBrandDTO = brandService.update(brandDTO);
        return new ResponseEntity<>(optionalBrandDTO.get(), HttpStatus.OK);
    }


    @PatchMapping(value = "/brands/{id}", consumes = {"application/json", "application/merge-path+json"})
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<BrandDTO> partialUpdate(@PathVariable(value = "id", required = false) Long id, @RequestBody BrandDTO brandDTO) {
        log.debug("partial update request {}", brandDTO);
        if (brandDTO.getId() == null || !Objects.equals(id, brandDTO.getId())) {
            throw new BadRequestAlertException("invalid id");
        }
        if (!brandRepository.existsById(id)) {
            throw new BadRequestAlertException("not found category");
        }
        Optional<Brand> existsName = brandRepository.findByName(brandDTO.getName());
        if (existsName.isPresent() && !checkId(existsName, brandDTO)) {
            throw new BrandNameAlreadyUsedException();
        }
        Optional<BrandDTO> optionalBrandDTO = brandService.partialUpdate(brandDTO);
        return new ResponseEntity<>(optionalBrandDTO.get(), HttpStatus.OK);

    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandDTO>> findAll() {
        log.debug("request list all ");
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<BrandDTO> findOne(@PathVariable Long id) {
        log.debug("request find brand {} ", id);
        Optional<BrandDTO> brandDTO = brandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brandDTO);
    }

    @DeleteMapping("/brands/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        if (!brandRepository.existsById(id)) {
            throw new BadRequestAlertException("not found category");
        }
        brandService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }


    @GetMapping("/brands/all/categories/{name}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<String>> getCategoryByBrand(@PathVariable String name) {
        if (!brandRepository.existsByName(name)) {
            throw new BadRequestAlertException("not found category");
        }
        List<String> categoryAsString = new ArrayList<>();
        brandRepository.findByName(name)
                .ifPresent((brand -> {
                    brand.getCategories()
                            .stream()
                            .map(Category::getName)
                            .forEach(categoryAsString::add);
                }));

        return new ResponseEntity<>(categoryAsString,HttpStatus.OK);
    }


    private boolean checkId(Optional<Brand> existsName, BrandDTO brandDTO) {
        return Objects.equals(existsName.get().getId(), brandDTO.getId());
    }

}
