package com.shop.service.dto;

import com.shop.model.Category;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryNode {

    private String name;
    private String alias;
    private Set<CategoryNode> child;

    public CategoryNode() {
    }

    public CategoryNode(Category category) {
        this.name = category.getName();
        this.alias=category.getAlias();
        this.child = category.getChildren()
                .stream()
                .filter(Objects::nonNull)
                .map(category1 -> {
                    CategoryNode categoryNode = new CategoryNode();
                    categoryNode.setName(category1.getName());
                    categoryNode.setAlias(category1.getAlias());
                    return categoryNode;
                }).collect(Collectors.toSet());
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

    public Set<CategoryNode> getChild() {
        return child;
    }

    public void setChild(Set<CategoryNode> child) {
        this.child = child;
    }


    @Override
    public String toString() {
        return "CategoryNode{" +
                "name='" + name + '\'' +
                '}';
    }
}
