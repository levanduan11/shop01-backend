package com.shop.service;

import com.shop.model.Category;
import com.shop.service.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Category create(CategoryDTO categoryDTO);

    Optional<Category> update(CategoryDTO category);

    Optional<Category> partialUpdate(CategoryDTO categoryDTO);

    List<Category> findAll();

    Optional<Category> findOne(Long id);

    void delete(Long id);


}
