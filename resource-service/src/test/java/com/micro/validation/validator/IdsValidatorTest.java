package com.micro.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IdsValidatorTest {
    @Test
    void testIsValidWithValidIds() {
        String validIds = "123,456,789";
        IdsValidator idsValidator = new IdsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdValidator mockIdValidator = mock(IdValidator.class);
        this.injectMockIdValidator(idsValidator, mockIdValidator);

        when(mockIdValidator.isValid(123L, context)).thenReturn(true);
        when(mockIdValidator.isValid(456L, context)).thenReturn(true);
        when(mockIdValidator.isValid(789L, context)).thenReturn(true);


        boolean result = idsValidator.isValid(validIds, context);

        assertTrue(result);
    }

    @Test
    void testIsValidWithNullValue() {
        String nullValue = null;
        IdsValidator idsValidator = new IdsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        boolean result = idsValidator.isValid(nullValue, context);

        assertFalse(result);
    }

    @Test
    void testIsValidWithEmptyString() {
        // Arrange
        String emptyString = "";
        IdsValidator idsValidator = new IdsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        boolean result = idsValidator.isValid(emptyString, context);
        assertFalse(result);
    }

    @Test
    void testIsValidWithInvalidPattern() {
        String invalidPattern = "123,abc,456";
        IdsValidator idsValidator = new IdsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        boolean result = idsValidator.isValid(invalidPattern, context);
        assertFalse(result);
    }

    @Test
    void testIsValidWithIndividualIdValidationFailure() {
        String ids = "123,456,789";
        IdsValidator idsValidator = new IdsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdValidator mockIdValidator = mock(IdValidator.class);
        this.injectMockIdValidator(idsValidator, mockIdValidator);

        when(mockIdValidator.isValid(123L, context)).thenReturn(true);
        when(mockIdValidator.isValid(456L, context)).thenReturn(false); // Fail this specific ID
        when(mockIdValidator.isValid(789L, context)).thenReturn(true);

        boolean result = idsValidator.isValid(ids, context);
        assertFalse(result);
    }

    @Test
    void testIsValidWithWhitespaceAroundIds() {
        String idsWithWhitespace = " 123 , 456 , 789 ";
        IdsValidator idsValidator = new IdsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdValidator mockIdValidator = mock(IdValidator.class);
        this.injectMockIdValidator(idsValidator, mockIdValidator);

        when(mockIdValidator.isValid(123L, context)).thenReturn(true);
        when(mockIdValidator.isValid(456L, context)).thenReturn(true);
        when(mockIdValidator.isValid(789L, context)).thenReturn(true);

        boolean result = idsValidator.isValid(idsWithWhitespace, context);
        assertTrue(result);
    }

    private void injectMockIdValidator(IdsValidator idsValidator, IdValidator mockIdValidator) {
        try {
            var field = IdsValidator.class.getDeclaredField("individualIdValidator");
            field.setAccessible(true);
            field.set(idsValidator, mockIdValidator);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock IdValidator", e);
        }
    }
}