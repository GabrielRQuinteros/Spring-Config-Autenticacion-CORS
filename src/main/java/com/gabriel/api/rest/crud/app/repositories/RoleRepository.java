package com.gabriel.api.rest.crud.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.gabriel.api.rest.crud.app.entities.Role;

public interface RoleRepository extends CrudRepository< Role, Long > {

    public Optional<Role> findByName( String name );
}
