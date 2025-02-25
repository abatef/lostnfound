package com.hatla2y.backend.exceptions.handlers;

import com.hatla2y.backend.exceptions.EmailAlreadyUsedException;
import com.hatla2y.backend.exceptions.InvalidOtpException;
import com.hatla2y.backend.exceptions.InvalidPhoneNumberException;
import com.hatla2y.backend.exceptions.InvalidUserIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class UserControllerAdvice {

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<String> handleEmailAlreadyUsedException(EmailAlreadyUsedException e) {
        return new ResponseEntity<>("Email Already Used: " + e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<String> handleInvalidUserIdException(InvalidUserIdException e) {
        return new ResponseEntity<>("Invalid User ID: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<String> handleInvalidPhoneNumberException(InvalidPhoneNumberException e) {
        return new ResponseEntity<>("Invalid Phone Number: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<String> handleInvalidOtpException(InvalidOtpException e) {
        return new ResponseEntity<>("Invalid OTP: " + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
}
