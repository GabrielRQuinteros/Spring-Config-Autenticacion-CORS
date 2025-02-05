package com.gabriel.api.rest.crud.app.validators.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gabriel.api.rest.crud.app.services.interfaces.UserService;
import com.gabriel.api.rest.crud.app.validators.interfaces.ExistsUserByUsernameDB;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsUserDBByUsernameImp implements ConstraintValidator< ExistsUserByUsernameDB, String > {


    @Autowired()
    UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return ! this.userService.findByUsername(username).isPresent();
    }

   

}
