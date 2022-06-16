package com.shop.advice;

import com.shop.errors.CategoryAliasAlreadyUsedException;
import com.shop.errors.CategoryNameAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class CategoryControllerAdvice {

    private static final String EXISTS_NAME = "CATEGORY_EXISTS_NAME";
    private static final String EXISTS_ALIAS = "CATEGORY_EXISTS_ALIAS";


    @ExceptionHandler(CategoryNameAlreadyUsedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseMessage handleDuplicateName(CategoryNameAlreadyUsedException e, WebRequest request) {

        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setTimestamp(new Date());
        message.setMessage(e.getMessage());
        message.setDescription(request.getDescription(false));
        message.setType(EXISTS_NAME);

        return message;
    }

    @ExceptionHandler(CategoryAliasAlreadyUsedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseMessage handleDuplicateAlias(CategoryAliasAlreadyUsedException e,WebRequest request){
        ErrorResponseMessage message=new ErrorResponseMessage();
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setTimestamp(new Date());
        message.setMessage(e.getMessage());
        message.setDescription(request.getDescription(false));
        message.setType(EXISTS_ALIAS);

        return message;
    }
}
