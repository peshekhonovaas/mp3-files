package com.micro.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdsValidatorTest {
    @Test
    void testIsValidWithValidIds() {
        IdsValidator validator = new IdsValidator();
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        String validIds = "123,456,789";

        boolean result = validator.isValid(validIds, context);
        assertTrue(result);
    }

    @Test
    void testIsValidWithEmptyString() {
        IdsValidator validator = new IdsValidator();
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);


        boolean result = validator.isValid("", context);
        assertFalse(result);
    }

    @Test
    void testIsValidWithNullValue() {
        IdsValidator validator = new IdsValidator();
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        boolean result = validator.isValid(null, context);
        assertFalse(result);
    }

    @Test
    void testIsValidWithInvalidPattern() {
        IdsValidator validator = new IdsValidator();
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        String invalidIds = "123,abc,789";

        boolean result = validator.isValid(invalidIds, context);
        assertFalse(result);
    }
}