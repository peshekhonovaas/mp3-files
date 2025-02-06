package com.micro.validation.validator;

import com.micro.validation.annotation.IdValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Pattern;

public class IdValidator implements ConstraintValidator<IdValid, Long> {
    private static final Pattern ID_RESTRICTION_PATTERN = Pattern.compile("^[1-9]\\d*$");

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return Objects.nonNull(id) && ID_RESTRICTION_PATTERN.matcher(String.valueOf(id)).matches();
    }
}