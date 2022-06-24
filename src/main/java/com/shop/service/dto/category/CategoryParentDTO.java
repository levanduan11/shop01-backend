package com.shop.service.dto.category;


import com.shop.model.Category;

public class CategoryParentDTO {

    private Long id;
    private String name;
    private String alias;

    public Long getId() {
        return id;
    }

    public CategoryParentDTO() {
    }

    public CategoryParentDTO(Category category) {
        this.id = category.getId();
        this.alias=category.getAlias();
        this.name=category.getName();
    }

    public CategoryParentDTO(Category category, String name) {
        this.id=category.getId();
        this.name = name;
        this.alias=category.getAlias();
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

    @Override
    public String toString() {
        return this.name;
    }


}
