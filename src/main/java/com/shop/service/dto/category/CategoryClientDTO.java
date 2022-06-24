package com.shop.service.dto.category;

import com.shop.model.Category;

import java.util.Set;
import java.util.stream.Collectors;

public class CategoryClientDTO {
    private Long id;
    private String name;
    private String alias;
    private String image;



    public CategoryClientDTO() {
    }

    public CategoryClientDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.alias = category.getAlias();
        this.image = category.getImage();

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
