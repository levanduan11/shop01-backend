package com.shop.web.rest.errors;

public class CategoryAliasAlreadyUsedException extends RuntimeException {

    public CategoryAliasAlreadyUsedException() {
        super("alias already used !");
    }
}
