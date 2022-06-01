package com.shop.web.rest.errors;

public class CategoryNameAlreadyUsedException extends RuntimeException{

    public CategoryNameAlreadyUsedException() {
        super("category name already used !");
    }
}
