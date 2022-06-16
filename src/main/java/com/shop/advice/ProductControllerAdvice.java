package com.shop.advice;

import com.shop.errors.ProductAliasAlreadyUsedException;
import com.shop.errors.ProductNameAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ProductControllerAdvice {
    private static final String EXISTS_NAME = "PRODUCT_EXISTS_NAME";
    private static final String EXISTS_ALIAS = "PRODUCT_EXISTS_ALIAS";

    @ExceptionHandler(ProductNameAlreadyUsedException.class)
    public ResponseEntity<ErrorResponseMessage> duplicateName(ProductNameAlreadyUsedException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setType(EXISTS_NAME);
        message.setMessage(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductAliasAlreadyUsedException.class)
    public ResponseEntity<ErrorResponseMessage> duplicateAlias(ProductAliasAlreadyUsedException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setType(EXISTS_ALIAS);
        message.setMessage(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
