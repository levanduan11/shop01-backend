package com.shop.errors;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException() {
        super("Not found product");
    }
}
