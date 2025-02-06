package com.micro.validation.validator;

import com.micro.validation.annotation.IdsValid;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.regex.Pattern;

public class IdsValidator implements ConstraintValidator<IdsValid, String> {
    private final IdValidator individualIdValidator = new IdValidator();
    private static final Pattern ID_RESTRICTION_PATTERN = Pattern.compile("^\\d+(,\\d+)*$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) return false;
        if (!ID_RESTRICTION_PATTERN.matcher(value).matches()) return false;
        String[] ids = value.split(",");
        return Arrays.stream(ids)
                .map(String::trim)
                .map(Long::parseLong)
                .allMatch(id -> individualIdValidator.isValid(id, context));
    }
}