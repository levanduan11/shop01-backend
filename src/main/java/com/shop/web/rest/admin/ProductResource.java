package com.shop.web.rest.admin;

import com.shop.errors.BadRequestAlertException;
import com.shop.errors.ProductAliasAlreadyUsedException;
import com.shop.errors.ProductNameAlreadyUsedException;
import com.shop.model.product.Product;
import com.shop.repository.ProductRepository;
import com.shop.security.AuthoritiesConstants;
import com.shop.service.Impl.ProductServiceImpl;
import com.shop.service.dto.product.ProductDTO;
import com.shop.service.dto.product.ProductForListDTO;
import com.shop.share.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/admin")
@CrossOrigin(origins = "*")
public class ProductResource {

    private static final Logger log = LoggerFactory.getLogger(ProductResource.class);
    private final ProductServiceImpl productService;
    private final ProductRepository productRepository;

    public ProductResource(ProductServiceImpl productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("/products")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO productDTO) {
        log.debug("request create product {} ", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID");
        }
        Product product = productService.create(productDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Product> update(@PathVariable(required = false, value = "id") final Long id, @Valid @RequestBody ProductDTO productDTO) {
        log.debug("request update product {} ", productDTO);
        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Not found product");
        }
        if (productDTO.getId() == null || !Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("In valid id");
        }
        Optional<Product> existingProduct = productRepository.findByName(productDTO.getName());
        if (existingProduct.isPresent() && !checkIdForUpdate(existingProduct, productDTO)) {
            throw new ProductNameAlreadyUsedException();
        }
        existingProduct = productRepository.findByAlias(productDTO.getAlias());
        if (existingProduct.isPresent() && !checkIdForUpdate(existingProduct, productDTO)) {
            throw new ProductAliasAlreadyUsedException();
        }
        Optional<Product> product = productService.update(productDTO);
        return ResponseUtil.wrapOrNotFound(product);
    }

    @PatchMapping("/products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Product> partialUpdate(@PathVariable(required = false, value = "id") final Long id, @Valid @RequestBody ProductDTO productDTO) {
        log.debug("request update some field product {} ", productDTO);
        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Not found product");
        }
        if (productDTO.getId() == null || !Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("In valid id");
        }
        Optional<Product> existingProduct = productRepository.findByName(productDTO.getName());
        if (existingProduct.isPresent() && !checkIdForUpdate(existingProduct, productDTO)) {
            throw new ProductNameAlreadyUsedException();
        }
        existingProduct = productRepository.findByAlias(productDTO.getAlias());
        if (existingProduct.isPresent() && !checkIdForUpdate(existingProduct, productDTO)) {
            throw new ProductAliasAlreadyUsedException();
        }
        Optional<Product> product = productService.partialUpdate(productDTO);
        return ResponseUtil.wrapOrNotFound(product);
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<ProductForListDTO>> findAll() {

        List<ProductForListDTO> listDTOS = productService.findAll();
        return ResponseEntity.ok(listDTOS);
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ProductDTO> findOne(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Not found product");
        }
        productService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    private boolean checkIdForUpdate(Optional<Product> product, ProductDTO productDTO) {
        return Objects.equals(product.get().getId(), productDTO.getId());
    }

}
