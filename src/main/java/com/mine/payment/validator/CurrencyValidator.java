package com.mine.payment.validator;

import com.mine.payment.annotation.CurrencyConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Currency;

/**
 * @stefanl
 */
public class CurrencyValidator implements ConstraintValidator<CurrencyConstraint, String> {
    @Override
    public void initialize(CurrencyConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = false;
        try {
            Currency.getInstance(value);
            valid = true;
        } catch (IllegalArgumentException iae) {
        }
        return valid;
    }
}
