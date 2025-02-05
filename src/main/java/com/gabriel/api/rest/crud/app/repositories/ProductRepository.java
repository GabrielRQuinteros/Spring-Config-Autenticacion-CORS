package com.gabriel.api.rest.crud.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.gabriel.api.rest.crud.app.entities.Product;

public interface ProductRepository extends CrudRepository <Product, Long> {

}
