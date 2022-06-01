package com.shop.service.Impl;

import com.shop.errors.CategoryNotFoundException;
import com.shop.model.Category;
import com.shop.repository.CategoryRepository;
import com.shop.service.ICategory;
import com.shop.service.dto.CategoryDTO;
import com.shop.service.dto.CategoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategory {
    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setName(categoryDTO.getName());
        if (categoryDTO.getAlias() == null) {
            category.setAlias(categoryDTO.getName().replace(" ", "_"));
        } else {
            category.setAlias(categoryDTO.getAlias());
        }
        category.setImage(categoryDTO.getImage());
        category.setEnabled(categoryDTO.isEnabled());

        Long parentId = categoryDTO.getParent_id();
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(CategoryNotFoundException::new);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }
        updateHelper(category);
        return category;
    }

    @Override
    public Optional<Category> update(CategoryDTO categoryDTO) {

        return categoryRepository
                .findById(categoryDTO.getId())
                .map(category -> {
                    if (categoryDTO.getName() != null) {
                        category.setName(categoryDTO.getName());
                    }
                    if (categoryDTO.getAlias() != null) {
                        category.setAlias(categoryDTO.getAlias());
                    }

                    if (categoryDTO.getImage() != null) {
                        category.setImage(categoryDTO.getImage());
                    }
                    category.setEnabled(categoryDTO.isEnabled());
                    Long parentId = categoryDTO.getParent_id();
                    if (parentId != null) {
                        Category parent = categoryRepository.findById(parentId)
                                .orElseThrow(CategoryNotFoundException::new);
                        category.setParent(parent);
                    } else {
                        category.setParent(null);
                    }
                    log.info("updated category {} ", category);
                    updateUseStack(category);
                    return category;
                });

    }

    private void updateUseStack(Category root) {
        Stack<Category> categories = new Stack<>();
        categories.push(root);
        while (!categories.isEmpty()) {
            Category r = categories.pop();
            updateHelper(r);
            Set<Category> child = r.getChildren();
            if (child.size() > 0) {
                child.forEach(categories::push);
            }
        }
    }

    private void updateHelper(Category root) {
        Category parent = root.getParent();
        if (parent != null) {
            setAllParentId(root, parent);
        } else {
            root.setAllParentId(null);
        }
        categoryRepository.save(root);

    }

    private void setAllParentId(Category category, Category parent) {
        String allPParent = parent.getAllParentId();
        String parentIdAsString = String.valueOf(parent.getId());
        String allParent = allPParent != null ? allPParent : "-";
        allParent = allParent.concat(parentIdAsString).concat("-");
        category.setAllParentId(allParent);
    }


    @Override
    public Optional<Category> partialUpdate(CategoryDTO categoryDTO) {
        log.info("partialUpdate category info {} ", categoryDTO);


        return categoryRepository.findById(categoryDTO.getId())
                .map(existingCategory -> {
                    if (categoryDTO.getName() != null) {
                        existingCategory.setName(categoryDTO.getName());
                    }
                    if (categoryDTO.getAlias() != null) {
                        existingCategory.setAlias(categoryDTO.getAlias());
                    }
                    if (categoryDTO.getImage() != null) {
                        existingCategory.setImage(categoryDTO.getImage());
                    }
                    if (categoryDTO.isEnabled()) {
                        existingCategory.setEnabled(categoryDTO.isEnabled());
                    }
                    if (categoryDTO.getParent_id() != null) {
                        Category parent = categoryRepository
                                .findById(categoryDTO.getId())
                                .orElseThrow(CategoryNotFoundException::new);

                        existingCategory.setParent(parent);
                    }
                    return existingCategory;
                })
                .map(categoryRepository::save);
    }

    public CategoryNode getTreeForUseQueue(Category category) {
        CategoryNode node = new CategoryNode(category);

        Queue<Category> categories = new LinkedList<>();
        Set<Category> categories1 = category.getChildren();
        TreeSet<Category> categories2 = new TreeSet<>(Comparator.comparing(Category::getName));
        categories2.addAll(categories1);

        if (categories2.size() > 0) {
            categories.addAll(categories2);
        }

        Queue<CategoryNode> categoryNodes = new LinkedList<>();
        Set<CategoryNode> categoryNodes1 = node.getChild();
        TreeSet<CategoryNode> categoryNodes2 = new TreeSet<>(Comparator.comparing(CategoryNode::getName));
        categoryNodes2.addAll(categoryNodes1);

        if (categoryNodes2.size() > 0) {
            categoryNodes.addAll(categoryNodes2);
        }

        while (!categories.isEmpty()) {
            Category ca = categories.poll();
            CategoryNode caNode = categoryNodes.poll();
            Set<Category> child = ca.getChildren();

            TreeSet<Category> child2 = new TreeSet<>(Comparator.comparing(Category::getName));
            child2.addAll(child);

            if (child2.size() > 0) {
                Set<CategoryNode> categoryNodes11 = new TreeSet<>(Comparator.comparing(CategoryNode::getName));
                child2.forEach(c -> {
                    CategoryNode categoryNode = new CategoryNode();
                    categoryNode.setName(c.getName());
                    categoryNode.setAlias(c.getAlias());

                    categoryNodes11.add(categoryNode);
                    categories.add(c);
                    categoryNodes.add(categoryNode);

                });
                assert caNode != null;
                caNode.setChild(categoryNodes11);
            }
        }
        return node;
    }


    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<CategoryNode> allRoot() {
        return categoryRepository.findAllByParentIdIsNull()
                .stream()
                .map(this::getTreeForUseQueue)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findOne(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

}
