package com.shop.errors;

public class ProductAliasAlreadyUsedException extends RuntimeException{

    public ProductAliasAlreadyUsedException() {
        super("product alias already used !");
    }

}
