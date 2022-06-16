package com.shop.advice;

import com.shop.errors.BrandNameAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class BrandControllerAdvice {

    private static final String EXISTS_NAME = "BRAND_EXISTS_NAME";

    @ExceptionHandler(BrandNameAlreadyUsedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseMessage handleExitsName(BrandNameAlreadyUsedException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setMessage(e.getMessage());
        message.setTimestamp(new Date());
        message.setDescription(request.getDescription(false));
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setType(EXISTS_NAME);
        return message;
    }
}
