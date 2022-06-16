package com.shop.errors;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException() {
        super("invalid password");
    }
}
