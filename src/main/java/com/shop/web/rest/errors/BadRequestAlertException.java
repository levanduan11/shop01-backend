package com.shop.web.rest.errors;

public class BadRequestAlertException extends RuntimeException{

    public BadRequestAlertException(String message) {
        super(message);
    }
}
