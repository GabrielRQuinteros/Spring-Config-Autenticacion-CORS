package com.gabriel.api.rest.crud.app.dtos.products;

import com.gabriel.api.rest.crud.app.validators.IsRequired;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductDTO(
    Long id,
    
    @NotNull( message = "El campo name no puede ser nulo" )
    @NotBlank( message = "El campo name no puede ser vacío" )
    @Size( max = 200, min = 5 )
    String name,
    
    @NotNull(message = "El campo price no puede ser nulo")
    Double price,
    
    @IsRequired( message = "El campo description no puede ser vacío --> Usando validación Custom con Annotation" )
    @Size( min = 10, max = 2000, message = "El campo description debe tener entre 10 y 2000 caracteres" )
    @NotNull(message = "El campo description no puede ser nulo")
    String description,

    String sku
    ) { }
