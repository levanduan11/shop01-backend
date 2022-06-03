package com.shop.service.dto;


import com.shop.model.Category;

public class CategoryParentDTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public CategoryParentDTO() {
    }

    public CategoryParentDTO(Category category,String name) {
        this.id=category.getId();
        this.name = name;
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

    @Override
    public String toString() {
        return this.name;
    }
}
