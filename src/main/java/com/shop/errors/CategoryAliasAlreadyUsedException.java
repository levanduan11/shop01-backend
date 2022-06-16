package com.shop.errors;

public class CategoryAliasAlreadyUsedException extends RuntimeException {

    public CategoryAliasAlreadyUsedException() {
        super("alias already used !");
    }
}
