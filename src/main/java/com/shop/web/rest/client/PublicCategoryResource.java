package com.shop.web.rest.client;

import com.shop.errors.CategoryNotFoundException;
import com.shop.model.Category;
import com.shop.repository.CategoryRepository;
import com.shop.service.Impl.CategoryServiceImpl;
import com.shop.service.dto.category.CategoryNode;
import com.shop.service.dto.category.CategoryParentDTO;
import com.shop.share.ResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/categories/{alias}")
    public ResponseEntity<CategoryNode> finOneAndChild(@PathVariable(value = "alias") String alias) {
        return ResponseUtil.wrapOrNotFound(categoryRepository.findByAlias(alias).map(CategoryNode::new));
    }

    @GetMapping("/categories/p/{aliasOrName}")
    public ResponseEntity<List<CategoryParentDTO>> findAllParent(@PathVariable(value = "aliasOrName") String aliasOrName) {
        Category category = getCategory(aliasOrName);
        LinkedList<CategoryParentDTO> parentDTOS = new LinkedList<>();
        if (category.getParent() != null) {
            while (category.getParent() != null) {
                Category parent = category.getParent();
                CategoryParentDTO dto = new CategoryParentDTO(parent);
                parentDTOS.addFirst(dto);
                category = category.getParent();
            }
        }
        return ResponseEntity.ok(parentDTOS);
    }

    private Category getCategory(String aliasOrName) {
        Optional<Category> category = categoryRepository.findByAlias(aliasOrName);
        if (!category.isPresent()) {
            category = categoryRepository.findByName(aliasOrName);
        }
        if (!category.isPresent()) {
            throw new CategoryNotFoundException();
        }
        return category.get();
    }

}
