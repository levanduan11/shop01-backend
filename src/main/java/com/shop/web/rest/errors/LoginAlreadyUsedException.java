package com.shop.web.rest.errors;

public class LoginAlreadyUsedException extends RuntimeException{

    public LoginAlreadyUsedException() {
        super("username already existing");
    }
}
