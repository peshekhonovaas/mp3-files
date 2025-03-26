package com.micro.validation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdValidatorTest {
    @Test
    void isValidShouldReturnTrueWhenValidIdProvided() {
        IdValidator validator = new IdValidator();
        Long validId = 12345L;

        boolean result = validator.isValid(validId, null);
        assertTrue(result, "Validator should return true for a valid ID.");
    }

    @Test
    void isValidShouldReturnFalseWhenIdIsNull() {
        IdValidator validator = new IdValidator();
        Long nullId = null;

        boolean result = validator.isValid(nullId, null);
        assertFalse(result, "Validator should return false for null ID.");
    }

    @Test
    void isValidShouldReturnFalseWhenIdIsNegative() {
        IdValidator validator = new IdValidator();
        Long negativeId = -12345L;
        boolean result = validator.isValid(negativeId, null);
        assertFalse(result, "Validator should return false for a negative ID.");
    }

    @Test
    void isValidShouldReturnFalseWhenIdIsZero() {
        IdValidator validator = new IdValidator();
        Long zeroId = 0L;

        boolean result = validator.isValid(zeroId, null);
        assertFalse(result, "Validator should return false for an ID of 0.");
    }
}