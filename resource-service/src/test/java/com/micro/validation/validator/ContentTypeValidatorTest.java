package com.micro.validation.validator;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentTypeValidatorTest {
    @Test
    void isValidShouldReturnTrueWhenContentTypeMatches() {
        ContentTypeValidator validator = new ContentTypeValidator();
        String validContentType = "audio/mpeg";

        boolean result = validator.isValid(validContentType, null);
        assertTrue(result);
    }

    @Test
    void isValidShouldReturnFalseWhenContentTypeIsNull() {
        ContentTypeValidator validator = new ContentTypeValidator();

        boolean result = validator.isValid(null, null);
        assertFalse(result);
    }

    @Test
    void isValidShouldReturnFalseWhenContentTypeDoesNotMatch() {
        ContentTypeValidator validator = new ContentTypeValidator();
        String invalidContentType = "video/mp4";

        boolean result = validator.isValid(invalidContentType, null);
        assertFalse(result);
    }

    @Test
    void isValidShouldReturnFalseWhenContentTypeIsEmptyString() {
        ContentTypeValidator validator = new ContentTypeValidator();

        boolean result = validator.isValid(StringUtils.EMPTY, null);
        assertFalse(result);
    }
}