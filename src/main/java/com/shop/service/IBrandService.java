package com.shop.service;

import com.shop.model.Brand;
import com.shop.service.dto.BrandDTO;

import java.util.List;
import java.util.Optional;

public interface IBrandService {

    Brand create(BrandDTO brandDTO);

    Optional<BrandDTO> update(BrandDTO brandDTO);

    Optional<BrandDTO> partialUpdate(BrandDTO brandDTO);

    List<BrandDTO> findAll();

    Optional<BrandDTO> findOne(Long id);

    void delete(Long id);
}
