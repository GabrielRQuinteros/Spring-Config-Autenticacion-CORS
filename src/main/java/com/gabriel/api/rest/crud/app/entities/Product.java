package com.gabriel.api.rest.crud.app.entities;

import com.gabriel.api.rest.crud.app.dtos.products.ProductDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {


    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    @Id()
    Long id;

    @NotEmpty
    @Size(max =  200, min = 5 )
    String name;

    @NotNull
    Double price;

    @NotNull
    @Size( max = 2000 )
    String description;

    private String sku;

    public Product loadFromDTO(ProductDTO productDTO) {
        this.name = productDTO.name();
        this.description = productDTO.description();
        this.price = productDTO.price();
        this.sku = productDTO.sku();
        return this;
    }
    

}
