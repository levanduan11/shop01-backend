package com.shop.service.Impl;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException() {
        super("invalid password");
    }
}
