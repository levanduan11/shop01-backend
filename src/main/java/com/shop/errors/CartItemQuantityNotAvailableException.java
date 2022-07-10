package com.shop.errors;

public class CartItemQuantityNotAvailableException extends RuntimeException{

    public CartItemQuantityNotAvailableException(String message) {
        super(message);
    }
}
