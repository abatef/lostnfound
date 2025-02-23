package com.hatla2y.backend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
