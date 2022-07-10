package com.shop.errors;

public class CustomerNotFoundException extends RuntimeException{

    public CustomerNotFoundException() {
        super("Not found customer");
    }
}
