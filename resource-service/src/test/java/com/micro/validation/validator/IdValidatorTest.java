package com.micro.validation.validator;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdValidatorTest {

    @Test
    void testIsValidWithValidIdReturnsTrue() {
        IdValidator idValidator = new IdValidator();
        Long validId = 12345L;

        boolean result = idValidator.isValid(validId, null);
        assertTrue(result);
    }

    @Test
    void testIsValidWithNullIdReturnsFalse() {
        IdValidator idValidator = new IdValidator();
        boolean result = idValidator.isValid(null, null);
        assertFalse(result);
    }

    @Test
    void testIsValidWithNegativeIdReturnsFalse() {
        IdValidator idValidator = new IdValidator();
        Long negativeId = -123L;

        boolean result = idValidator.isValid(negativeId, null);
        assertFalse(result);
    }

    @Test
    void testIsValidWithZeroIdReturnsFalse() {
        IdValidator idValidator = new IdValidator();
        Long zeroId = 0L;

        boolean result = idValidator.isValid(zeroId, null);
        assertFalse(result);
    }
}