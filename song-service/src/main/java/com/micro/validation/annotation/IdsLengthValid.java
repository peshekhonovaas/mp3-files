package com.micro.validation.annotation;

import com.micro.validation.validator.IdsLengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdsLengthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdsLengthValid {
    String message() default "ID list exceeds length restrictions";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max() default Integer.MAX_VALUE;
}
