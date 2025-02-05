package com.gabriel.api.rest.crud.app.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint( validatedBy = { RequiredValidation.class } )
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD } )
public @interface IsRequired {

    String message() default "Este campo es requerido usando anotaciones";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
