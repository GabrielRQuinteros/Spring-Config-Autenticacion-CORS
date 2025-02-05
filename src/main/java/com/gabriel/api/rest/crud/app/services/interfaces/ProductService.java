package com.gabriel.api.rest.crud.app.services.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gabriel.api.rest.crud.app.dtos.products.ProductDTO;
import com.gabriel.api.rest.crud.app.entities.Product;

@Service
public interface ProductService {

    Optional<Product> findById ( Long id );

    List<Product> findAll();

    Product save( ProductDTO product );

    Optional<Product> update( Long id, ProductDTO product );

    Optional<Product> deleteById ( Long id );

    void delete ( Product product );




}
