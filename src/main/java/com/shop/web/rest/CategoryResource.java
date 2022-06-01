package com.shop.web.rest;

import com.shop.model.Category;
import com.shop.repository.CategoryRepository;
import com.shop.security.AuthoritiesConstants;
import com.shop.service.Impl.CategoryServiceImpl;
import com.shop.service.dto.CategoryDTO;
import com.shop.service.dto.CategoryNode;
import com.shop.share.HeaderUtil;
import com.shop.share.PaginationUtil;
import com.shop.share.ResponseUtil;
import com.shop.web.rest.errors.BadRequestAlertException;
import com.shop.web.rest.errors.CategoryAliasAlreadyUsedException;
import com.shop.web.rest.errors.CategoryNameAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
public class CategoryResource {

    private final static Logger log = LoggerFactory.getLogger(CategoryResource.class);
    private final String applicationName = "e-comerShop";
    private final CategoryRepository categoryRepository;
    private final CategoryServiceImpl categoryService;
    private static final String ENTITY_NAME = "category";

    public CategoryResource(CategoryRepository categoryRepository, CategoryServiceImpl categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO) throws URISyntaxException {
        log.info("create category request {}", categoryDTO);

        if (categoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID");
        }
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new CategoryNameAlreadyUsedException();
        }
        if (categoryRepository.existsByAlias(categoryDTO.getAlias())) {
            throw new CategoryAliasAlreadyUsedException();
        }
        Category newCategory = categoryService.create(categoryDTO);
        return ResponseEntity
                .created(new URI("api/admin/categories/" + newCategory.getId()))
                .headers(HeaderUtil.createAlert(applicationName, "category.created", newCategory.getId().toString()))
                .body(newCategory);
    }

    @PutMapping("/categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("update category request {} ", categoryDTO);
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory.isPresent() && !checkIdExistsForUpdate(existingCategory, categoryDTO)) {
            throw new CategoryNameAlreadyUsedException();
        }
        existingCategory = categoryRepository.findByAlias(categoryDTO.getAlias());
        if (existingCategory.isPresent() && !checkIdExistsForUpdate(existingCategory, categoryDTO)) {
            throw new CategoryAliasAlreadyUsedException();
        }
        Optional<Category> category = categoryService.update(categoryDTO);
        return ResponseUtil
                .wrapOrNotFound(category, HeaderUtil.createAlert(applicationName, "category.updated", categoryDTO.getId().toString()));


    }

    @PatchMapping(value = "/categories/{id}", consumes = {"application/json", "application/merge-patch+json"})
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Category> partialUpdateCategory(@PathVariable(value = "id", required = false) final Long id,
                                                          @RequestBody CategoryDTO categoryDTO) {
        log.debug("update some field category {} ", id, categoryDTO);
        if (categoryDTO.getId() == null) {
            throw new BadRequestAlertException("invalid id");
        }
        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("not found category");
        }
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory.isPresent() && !checkIdExistsForUpdate(existingCategory, categoryDTO)) {
            throw new CategoryNameAlreadyUsedException();
        }
        existingCategory = categoryRepository.findByAlias(categoryDTO.getAlias());
        if (existingCategory.isPresent() && !checkIdExistsForUpdate(existingCategory, categoryDTO)) {
            throw new CategoryAliasAlreadyUsedException();
        }
        Optional<Category> category = categoryService.partialUpdate(categoryDTO);

        return ResponseUtil.wrapOrNotFound(
                category,
                HeaderUtil.createAlert(applicationName, ENTITY_NAME, categoryDTO.getId().toString())
        );
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<CategoryDTO>> listByPage(@ParameterObject Pageable pageable) {

        log.debug("request get all list category ");
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        log.debug("uri=====>>>> {} ", builder.toUriString());
        System.out.println("uri=====>>>> {} " + builder.toUriString());
        Page<CategoryDTO> page = categoryRepository.findAll(pageable)
                .map(CategoryDTO::new);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    @GetMapping("/categories/all")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<CategoryDTO>> listAllCategory() {

        List<CategoryDTO> categoryDTOS = categoryService
                .findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOS);
    }

    @GetMapping("/categories/alll")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<CategoryNode>> test() {

        List<CategoryNode> CategoryNodes = categoryService.allRoot();

        return ResponseEntity.ok(CategoryNodes);
    }

    @GetMapping("/categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        Optional<CategoryDTO> categoryDTO = categoryService.findOne(id).map(CategoryDTO::new);
        return ResponseUtil.wrapOrNotFound(categoryDTO);

    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createAlert(applicationName, ENTITY_NAME, id.toString()))
                .build();

    }


    private boolean checkIdExistsForUpdate(Optional<Category> category, CategoryDTO categoryDTO) {
        return Objects.equals(category.get().getId(), categoryDTO.getId());
    }


}

