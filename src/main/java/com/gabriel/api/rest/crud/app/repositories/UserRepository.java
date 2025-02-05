package com.gabriel.api.rest.crud.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.gabriel.api.rest.crud.app.entities.User;

public interface UserRepository extends CrudRepository< User , Long> {

    Optional<User> findByUsername( String username );


}