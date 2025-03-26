package com.micro.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleMetadataParsingExceptionShouldReturnCorrectResponse() {
        MetadataParsingException exception = new MetadataParsingException("Metadata parsing failed", new Exception());
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMetadataParsingException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Metadata parsing failed", response.getBody().errorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().errorCode());
    }

    @Test
    void handleMessageFailedExceptionShouldReturnCorrectResponse() {
        MessageFailedException exception = new MessageFailedException("Message delivery failed",  new Exception());

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMessageFailedException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Message delivery failed", response.getBody().errorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().errorCode());
    }

    @Test
    void handleConfigurationFailedExceptionShouldReturnCorrectResponse() {
        ConfigurationFailedException exception = new ConfigurationFailedException("Configuration has failed",  new Exception());

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConfigurationFailedException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Configuration has failed", response.getBody().errorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().errorCode());
    }
}