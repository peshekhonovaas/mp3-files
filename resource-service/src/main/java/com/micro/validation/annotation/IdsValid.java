package com.micro.validation.annotation;

import com.micro.validation.validator.IdsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdsValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdsValid {
    String message() default "The provided IDs are invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}