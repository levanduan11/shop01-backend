package com.shop.service.Impl;

import com.shop.model.Brand;
import com.shop.model.Category;
import com.shop.repository.BrandRepository;
import com.shop.repository.CategoryRepository;
import com.shop.service.IBrandService;
import com.shop.service.dto.BrandDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements IBrandService {
    private static final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public BrandServiceImpl(BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Brand create(BrandDTO brandDTO) {
        log.debug("request new brand {} ", brandDTO);
        Brand brand = new Brand();
        brand.setName(brandDTO.getName());
        brand.setLogo(brandDTO.getLogo());
        if (brandDTO.getCategories() != null) {
            Set<Category> categories = brandDTO.getCategories()
                    .stream()
                    .map(x -> x.replace("-", "").replace("+", ""))
                    .map(categoryRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            brand.setCategories(categories);
        }
        brandRepository.save(brand);
        return brand;
    }

    @Override
    public Optional<BrandDTO> update(BrandDTO brandDTO) {
        return Optional.of(brandRepository.findById(brandDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(brand -> {
                    brand.setName(brandDTO.getName());
                    brand.setLogo(brandDTO.getLogo());
                    Set<Category> categories = brand.getCategories();
                    categories.clear();
                    if (brandDTO.getCategories() != null) {
                        brandDTO.getCategories().stream()
                                .map(categoryRepository::findByName)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .forEach(categories::add);
                    }
                    brandRepository.save(brand);
                    return brand;
                }).map(BrandDTO::new);
    }

    @Override
    public Optional<BrandDTO> partialUpdate(BrandDTO brandDTO) {
        return brandRepository.findById(brandDTO.getId())
                .map(brand -> {
                    if (brandDTO.getName() != null) {
                        brand.setName(brandDTO.getName());
                    }
                    if (brandDTO.getLogo() != null) {
                        brand.setLogo(brandDTO.getLogo());
                    }
                    if (brandDTO.getCategories() != null) {
                        Set<Category> categories = brand.getCategories();
                        categories.clear();
                        brandDTO.getCategories()
                                .stream()
                                .map(categoryRepository::findByName)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .forEach(categories::add);
                    }
                    brandRepository.save(brand);
                    return brand;
                })
                .map(BrandDTO::new);

    }

    @Override
    public List<BrandDTO> findAll() {
        return brandRepository.findAll().stream().map(BrandDTO::new).collect(Collectors.toList());
    }

    @Override
    public Optional<BrandDTO> findOne(Long id) {
        return brandRepository.findById(id).map(BrandDTO::new);
    }

    @Override
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }


}
