package com.shop.errors;

public class BadRequestAlertException extends RuntimeException{

    public BadRequestAlertException(String message) {
        super(message);
    }
}
