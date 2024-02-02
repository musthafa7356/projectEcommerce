package com.example.library.exception;

public class DisabledProductExistsException extends RuntimeException {

    public DisabledProductExistsException(String message) {
        super(message);
    }
}
