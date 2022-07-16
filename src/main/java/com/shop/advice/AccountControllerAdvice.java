package com.shop.advice;

import com.shop.errors.AccountInvalidException;
import com.shop.errors.EmailAlreadyUsedException;
import com.shop.errors.UsernameAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AccountControllerAdvice {
    private static final String EXISTS_USERNAME = "USERNAME_USED";
    private static final String EXISTS_EMAIL = "EMAIL_USED";

    @ExceptionHandler(AccountInvalidException.class)
    public ResponseEntity<ErrorResponseMessage> invalidAccount(AccountInvalidException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setMessage(e.getMessage());
        message.setType("PASSWORD_INVALID");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<ErrorResponseMessage> duplicateUsername(UsernameAlreadyUsedException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setMessage(e.getMessage());
        message.setType(EXISTS_USERNAME);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponseMessage> duplicateEmail(EmailAlreadyUsedException e, WebRequest request) {
        ErrorResponseMessage message = new ErrorResponseMessage();
        message.setMessage(e.getMessage());
        message.setType(EXISTS_EMAIL);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


}
