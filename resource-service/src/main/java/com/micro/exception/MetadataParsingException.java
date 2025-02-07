package com.micro.exception;

public class MetadataParsingException extends RuntimeException {
    public MetadataParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}