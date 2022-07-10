package com.shop.advice;

import com.shop.errors.CartItemQuantityNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CartItemControllerAdvice {
    private static final String quantity = "quantity_not_available";
    @ExceptionHandler(CartItemQuantityNotAvailableException.class)
    public ResponseEntity<ErrorResponseMessage> cartQuantityHandler(CartItemQuantityNotAvailableException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setType(quantity);
        message.setMessage(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
