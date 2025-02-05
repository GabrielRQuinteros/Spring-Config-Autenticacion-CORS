package com.gabriel.api.rest.crud.app.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.gabriel.api.rest.crud.app.dtos.products.ProductDTO;


@Component
public class ProductValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        
        ProductDTO productDTO = (ProductDTO)target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "400", "Name is required - Validator");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "400", "Description is required - Validator");
        
        if( productDTO.price() == null )
            errors.rejectValue("price", "400", "Price is required - Validator");
        else {
            if( productDTO.price() < 0 )
            errors.rejectValue("price", "400", "Price must be greater than 0 - Validator");
        }
    }

}
