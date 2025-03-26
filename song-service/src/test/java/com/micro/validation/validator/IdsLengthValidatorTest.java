package com.micro.validation.validator;

import com.micro.validation.annotation.IdsLengthValid;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IdsLengthValidatorTest {
    @Test
    public void testIsValidWhenIdsIsValidReturnsTrue() {
        IdsLengthValid annotation = mock(IdsLengthValid.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdsLengthValidator validator = new IdsLengthValidator();
        int maxLength = 10;
        when(annotation.max()).thenReturn(maxLength);
        validator.initialize(annotation);

        String validIds = "12345";

        boolean result = validator.isValid(validIds, context);
        assertTrue(result);
    }

    @Test
    public void testIsValidWhenIdsIsTooLongReturnsFalse() {
        IdsLengthValid annotation = mock(IdsLengthValid.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdsLengthValidator validator = new IdsLengthValidator();
        int maxLength = 5;
        when(annotation.max()).thenReturn(maxLength);
        validator.initialize(annotation);

        String tooLongIds = "123456";
        boolean result = validator.isValid(tooLongIds, context);
        assertFalse(result);
    }

    @Test
    public void testIsValidWhenIdsIsEmptyReturnsFalse() {
        IdsLengthValid annotation = mock(IdsLengthValid.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdsLengthValidator validator = new IdsLengthValidator();
        int maxLength = 10;
        when(annotation.max()).thenReturn(maxLength);
        validator.initialize(annotation);

        String emptyIds = "";
        boolean result = validator.isValid(emptyIds, context);
        assertFalse(result);
    }

    @Test
    public void testIsValidWhenIdsIsNullReturnsFalse() {
        IdsLengthValid annotation = mock(IdsLengthValid.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        IdsLengthValidator validator = new IdsLengthValidator();
        int maxLength = 10;
        when(annotation.max()).thenReturn(maxLength);
        validator.initialize(annotation);

        boolean result = validator.isValid(null, context);
        assertFalse(result);
    }
}