package com.micro.validation.validator;

import com.micro.validation.annotation.IdsLengthValid;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdsLengthValidatorTest {

    @Test
    void isValidReturnsTrueWhenStringIsNotEmptyAndWithinMaxLength() {
        IdsLengthValidator validator = new IdsLengthValidator();
        IdsLengthValid annotation = Mockito.mock(IdsLengthValid.class);
        Mockito.when(annotation.max()).thenReturn(10);
        validator.initialize(annotation);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        String validInput = "12345";

        boolean result = validator.isValid(validInput, context);
        assertTrue(result);
    }

    @Test
    void isValidReturnsFalseWhenStringExceedsMaxLength() {
        IdsLengthValidator validator = new IdsLengthValidator();
        IdsLengthValid annotation = Mockito.mock(IdsLengthValid.class);
        Mockito.when(annotation.max()).thenReturn(5);
        validator.initialize(annotation);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        String invalidInput = "123456789";

        boolean result = validator.isValid(invalidInput, context);
        assertFalse(result);
    }

    @Test
    void isValidReturnsFalseWhenStringIsNull() {
        IdsLengthValidator validator = new IdsLengthValidator();
        IdsLengthValid annotation = Mockito.mock(IdsLengthValid.class);
        Mockito.when(annotation.max()).thenReturn(10);
        validator.initialize(annotation);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        String nullInput = null;

        boolean result = validator.isValid(nullInput, context);
        assertFalse(result);
    }

    @Test
    void isValidReturnsFalseWhenStringIsEmpty() {
        IdsLengthValidator validator = new IdsLengthValidator();
        IdsLengthValid annotation = Mockito.mock(IdsLengthValid.class);
        Mockito.when(annotation.max()).thenReturn(10);
        validator.initialize(annotation);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        String emptyInput = "";

        boolean result = validator.isValid(emptyInput, context);
        assertFalse(result);
    }
}