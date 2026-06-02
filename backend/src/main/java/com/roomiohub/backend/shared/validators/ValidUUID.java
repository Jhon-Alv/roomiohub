package com.roomiohub.backend.shared.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UuidValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {

    String message() default "El formato del UUID es inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
