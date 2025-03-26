package com.micro.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void handleValidationExceptionsShouldReturnValidationErrorResponse() {
        FieldError fieldError1 = new FieldError("TestObject", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("TestObject", "field2", "must be greater than 0");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("field1", "must not be null");
        expectedErrors.put("field2", "must be greater than 0");

        ResponseEntity<ValidationErrorResponse> response =
                handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().errorMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().errorCode());
        assertEquals(expectedErrors, response.getBody().details());
    }

    @Test
    void handleValidationExceptions_shouldHandleEmptyErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of());

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ValidationErrorResponse> response =
                handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().errorMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().errorCode());
        assertEquals(new HashMap<>(), response.getBody().details());
    }
}