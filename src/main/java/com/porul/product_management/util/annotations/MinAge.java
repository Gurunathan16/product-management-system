package com.porul.product_management.util.annotations;

import com.porul.product_management.util.annotations.validators.MinAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge
{
    int value();
    String message() default "User must be at least {value} years old.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
