package com.hatla2y.backend.exceptions.handlers;

import com.hatla2y.backend.exceptions.InvalidItemIdException;
import com.hatla2y.backend.exceptions.NotOwnedItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class ItemControllerAdvice {

    @ExceptionHandler(NotOwnedItemException.class)
    public ResponseEntity<String> handleNotOwnedItemException(NotOwnedItemException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidItemIdException.class)
    public ResponseEntity<String> handleInvalidItemIdException(InvalidItemIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
