package com.shop.errors;

public class UsernameAlreadyUsedException extends RuntimeException{

    public UsernameAlreadyUsedException() {
        super("username is already used !");
    }
}
