package com.shop.service.Impl;

public class EmailAlreadyUsedException extends RuntimeException {
    private static final long serialVersionUID=1l;

    public EmailAlreadyUsedException() {
        super("Email is already is used");
    }
}
