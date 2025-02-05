package com.gabriel.api.rest.crud.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.api.rest.crud.app.dtos.products.ProductDTO;
import com.gabriel.api.rest.crud.app.entities.Product;
import com.gabriel.api.rest.crud.app.services.interfaces.ProductService;
import com.gabriel.api.rest.crud.app.validators.ProductValidator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController()
@RequestMapping( "/api/products" )
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductValidator productDTOValidator;

    @GetMapping()
    @PreAuthorize( "hasAnyRole('ADMIN', 'USER')" )
    public List<Product> getProducts() {
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyRole('ADMIN', 'USER')" )
    public ResponseEntity<?> findById( @Min(1) @PathVariable Long id ) {
        Optional<Product> prod= this.productService.findById(id);
        if( prod.isPresent() )
            return ResponseEntity.ok( prod.get() );
        return ResponseEntity.notFound().build();
        
    }

    @PostMapping
    @PreAuthorize( "hasRole('ADMIN')" )
    public ResponseEntity<?>create( @Valid @RequestBody ProductDTO product, BindingResult validationResult ) {
        // productDTOValidator.validate(product, validationResult);
        if (validationResult.hasErrors()) {
            return validate( validationResult );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body( this.productService.save(product) );
    }

    @PutMapping("/{id}")
    @PreAuthorize( "hasRole('ADMIN')" )
    public ResponseEntity<?>update( @Valid @RequestBody ProductDTO productDTO, BindingResult validationResult,  @PathVariable @Min(1) Long id ) {
        // productDTOValidator.validate(id, validationResult);
        if (validationResult.hasErrors()) {
            return validate( validationResult );
        }

        Optional<Product> productOpt = this.productService.update(id, productDTO);
        if( productOpt.isPresent() )
            return ResponseEntity.ok(productOpt.get());
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize( "hasRole('ADMIN')" )
    public ResponseEntity<?> delete(@PathVariable @Min(1) @NotNull Long id) {
        Optional<Product> productOpt = this.productService.deleteById(id);
        if (productOpt.isPresent()) {
            return ResponseEntity.ok(productOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validate(BindingResult validationResult) {
        Map<String, String> errors = new HashMap<>();
        validationResult.getFieldErrors().forEach((err) -> {
            errors.put( err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);

    }
    

}
