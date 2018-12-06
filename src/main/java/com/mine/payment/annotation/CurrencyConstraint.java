package com.mine.payment.annotation;

import com.mine.payment.validator.CurrencyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @stefanl
 */
@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyConstraint {
    String message() default "Invalid currency. Only EUR is accepted.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
