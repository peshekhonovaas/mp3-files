package com.micro.exception;

public class MessageFailedException extends RuntimeException {
    public MessageFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
