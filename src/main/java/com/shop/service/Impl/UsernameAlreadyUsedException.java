package com.shop.service.Impl;

public class UsernameAlreadyUsedException extends RuntimeException{

    public UsernameAlreadyUsedException() {
        super("username is already used !");
    }
}
