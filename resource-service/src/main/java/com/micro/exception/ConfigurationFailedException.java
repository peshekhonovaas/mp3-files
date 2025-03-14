package com.micro.exception;

public class ConfigurationFailedException extends RuntimeException {
    public ConfigurationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}