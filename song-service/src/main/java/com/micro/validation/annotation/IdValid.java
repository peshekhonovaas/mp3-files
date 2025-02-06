package com.micro.validation.annotation;

import com.micro.validation.validator.IdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdValid {
    String message() default "The provided ID is invalid (contains letters, decimals, is negative, or zero)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}