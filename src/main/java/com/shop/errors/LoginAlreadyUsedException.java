package com.shop.errors;

public class LoginAlreadyUsedException extends RuntimeException{

    public LoginAlreadyUsedException() {
        super("username already existing");
    }
}
