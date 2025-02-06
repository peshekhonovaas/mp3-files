package com.micro.exception;

import java.util.Map;

public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> details;

    public ValidationErrorResponse(String errorMessage, Map<String, String> details, int errorCode) {
        super(errorMessage, errorCode);
        this.details = details;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
