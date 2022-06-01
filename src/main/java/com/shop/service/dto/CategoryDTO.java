package com.shop.service.dto;

import com.shop.model.Category;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryDTO {

    private Long id;
    private String name;
    private String alias;

    private boolean enabled;

    private String image;
    private Long parent_id;

    private Set<String>child;

    public CategoryDTO() {
    }

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.alias = category.getAlias();
        this.enabled = category.isEnabled();
        this.image = category.getImage();
        this.parent_id = category.getParent() != null ? category.getParent().getId() : null;
        this.child=category.getChildren()
                .stream()
                .filter(Objects::nonNull)
                .map(Category::getName)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<String> getChild() {
        return child;
    }

    public void setChild(Set<String> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", enabled=" + enabled +
                ", image='" + image + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }
}
