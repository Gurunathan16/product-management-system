package com.porul.product_management.util.annotations.validators;

import com.porul.product_management.util.annotations.MinAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate>
{

    private Integer ageLimit;

    @Override
    public void initialize(MinAge constraintAnnotation)
    {
        this.ageLimit = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext constraintValidatorContext)
    {
        if(dob == null)
            return false;

        return Period.between(dob, LocalDate.now()).getYears() >= ageLimit;
    }

}
