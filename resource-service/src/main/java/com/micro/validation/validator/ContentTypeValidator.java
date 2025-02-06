package com.micro.validation.validator;

import com.micro.validation.annotation.ContentTypeValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ContentTypeValidator implements ConstraintValidator<ContentTypeValid, String> {
    private static final String CONTENT_TYPE = "audio/mpeg";

    @Override
    public boolean isValid(String contentType, ConstraintValidatorContext context) {
        return Objects.nonNull(contentType) && CONTENT_TYPE.equals(contentType);
    }
}
