package com.shop.web.rest.client;

import com.shop.errors.BrandNotFoundException;
import com.shop.errors.CategoryNotFoundException;
import com.shop.model.Brand;
import com.shop.model.Category;
import com.shop.repository.BrandRepository;
import com.shop.repository.CategoryRepository;
import com.shop.repository.ProductRepository;
import com.shop.service.Impl.ProductServiceImpl;
import com.shop.service.dto.product.ProductDTO;
import com.shop.share.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/client")
@CrossOrigin(value = "*")
public class PublicProductResource {

    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    private EntityManager entityManager;

    public PublicProductResource(ProductRepository productRepository, ProductServiceImpl productService, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/c/{category_alias}")
    public ResponseEntity<Page<ProductDTO>> listByCategory(@PathVariable(name = "category_alias") String alias, Pageable pageable) {
        Category category = categoryRepository.findByAlias(alias).orElseThrow(CategoryNotFoundException::new);
        String all = "-".concat(String.valueOf(category.getId())).concat("-");
        Page<ProductDTO> page = productRepository.listByCategory(category.getId(), all, pageable)
                .map(ProductDTO::new);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/b/{id}")
    public ResponseEntity<Page<ProductDTO>> listByBrand(@PathVariable(name = "id") Long id, @PageableDefault(sort = "name") Pageable pageable) {

        Optional<Brand> brand = brandRepository.findById(id);
        if (!brand.isPresent()) {
            throw new BrandNotFoundException();
        }
        Page<ProductDTO> page = productRepository.findAllByBrandId(id, pageable).map(ProductDTO::new);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/p/{product_alias}")
    public ResponseEntity<ProductDTO> viewDetail(@PathVariable(name = "product_alias") String alias) {
        Optional<ProductDTO> productDTO = productRepository.findByAlias(alias).map(ProductDTO::new);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> fullTextSearch(@RequestParam(value = "keyword") String keyword, Pageable pageable) throws InterruptedException {
        Page<ProductDTO> page = productService.fullTextSearch(keyword, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/p/new")
    public ResponseEntity<List<ProductDTO>> findNewProduct() {
        List<ProductDTO> newProducts = productRepository.findTop12OrderByCreatedTimeDesc()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(newProducts, HttpStatus.OK);
    }

}
