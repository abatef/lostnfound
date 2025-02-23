package com.hatla2y.backend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidUserIdException extends RuntimeException {
    public InvalidUserIdException(String message) {
        super(message);
    }
}
