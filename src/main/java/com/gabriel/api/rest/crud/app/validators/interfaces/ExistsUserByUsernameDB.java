package com.gabriel.api.rest.crud.app.validators.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gabriel.api.rest.crud.app.validators.implementations.ExistsUserDBByUsernameImp;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint( validatedBy = { ExistsUserDBByUsernameImp.class } )
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD } )
public @interface ExistsUserByUsernameDB {

    String message() default "El nombre de usuario ya esta en uso.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
