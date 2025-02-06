package com.micro.validation.validator;

import com.micro.validation.annotation.IdsLengthValid;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdsLengthValidator implements ConstraintValidator<IdsLengthValid, String> {
    private int max;

    @Override
    public void initialize(IdsLengthValid constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String ids, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(ids) && ids.length() <= max;
    }
}
