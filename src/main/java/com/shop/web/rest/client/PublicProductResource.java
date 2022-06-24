package com.shop.web.rest.client;

import com.shop.errors.CategoryNotFoundException;
import com.shop.model.Category;
import com.shop.model.product.Product;
import com.shop.repository.CategoryRepository;
import com.shop.repository.ProductRepository;
import com.shop.service.Impl.ProductServiceImpl;
import com.shop.service.dto.product.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/client")
@CrossOrigin(value = "*")
public class PublicProductResource {

    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;

    private final CategoryRepository categoryRepository;

    public PublicProductResource(ProductRepository productRepository, ProductServiceImpl productService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/c/{category_alias}")
    public ResponseEntity<Page<ProductDTO>> listByCategory(@PathVariable(name = "category_alias") String alias, @PageableDefault Pageable pageable) {
        Category category = categoryRepository.findByAlias(alias).orElseThrow(CategoryNotFoundException::new);
        String all = "-".concat(String.valueOf(category.getId())).concat("-");
        Page<ProductDTO> page = productRepository.listByCategory(category.getId(), all, pageable)
                .map(ProductDTO::new);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
