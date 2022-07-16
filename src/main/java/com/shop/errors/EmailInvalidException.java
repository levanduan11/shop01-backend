package com.shop.errors;

public class EmailInvalidException extends RuntimeException{

    public EmailInvalidException(String message) {
        super(message);
    }
}
