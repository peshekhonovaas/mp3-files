package com.micro.exception;

import com.micro.dto.ValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void handleValidationExceptionsShouldReturnValidationErrorResponse() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("TestObject", "field1", "Field1 is invalid");
        FieldError fieldError2 = new FieldError("TestObject", "field2", "Field2 cannot be null");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ValidationErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ValidationErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.errorMessage()).isEqualTo("Validation error");
        assertThat(body.errorCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("field1", "Field1 is invalid");
        expectedErrors.put("field2", "Field2 cannot be null");

        assertThat(body.details()).isEqualTo(expectedErrors);
    }
}