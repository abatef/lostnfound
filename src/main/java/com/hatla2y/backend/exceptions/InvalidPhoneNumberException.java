package com.hatla2y.backend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
