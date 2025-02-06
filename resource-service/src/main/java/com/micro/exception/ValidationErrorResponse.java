package com.micro.exception;

import java.util.Map;

public record ValidationErrorResponse(String errorMessage, int errorCode, Map<String, String> details) {
}