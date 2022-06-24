package com.shop.errors;

public class AccountInvalidException extends RuntimeException{

    public AccountInvalidException() {
        super("Invalid username or password");
    }
}
