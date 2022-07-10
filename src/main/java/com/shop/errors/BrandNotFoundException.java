package com.shop.errors;

public class BrandNotFoundException extends RuntimeException{

    public BrandNotFoundException() {
        super("not found brand ");
    }
}
