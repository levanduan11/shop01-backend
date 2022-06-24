package com.shop.web.rest.client;

import com.shop.repository.CategoryRepository;
import com.shop.service.Impl.CategoryServiceImpl;
import com.shop.service.dto.category.CategoryClientDTO;
import com.shop.service.dto.category.CategoryNode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/client")
@CrossOrigin(value = "*")
public class PublicCategoryResource {
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;

    public PublicCategoryResource(CategoryServiceImpl categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<CategoryNode> findAll() {
        return categoryService.allRoot();
    }


}
