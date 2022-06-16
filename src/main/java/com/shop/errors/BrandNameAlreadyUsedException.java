package com.shop.errors;

public class BrandNameAlreadyUsedException extends RuntimeException{

    public BrandNameAlreadyUsedException() {
        super("Brand Name Already Used !");
    }
}
