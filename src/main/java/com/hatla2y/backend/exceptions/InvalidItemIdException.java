package com.hatla2y.backend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidItemIdException extends RuntimeException {
    public InvalidItemIdException(String message) {
        super(message);
    }
}
