package com.hatla2y.backend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotOwnedItemException extends RuntimeException {
    public NotOwnedItemException(String message) {
        super(message);
    }
}
