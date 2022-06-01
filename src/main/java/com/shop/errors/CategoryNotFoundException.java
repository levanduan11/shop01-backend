package com.shop.errors;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException() {
        super("Category not found");
    }
}
