package com.gabriel.api.rest.crud.app.services.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gabriel.api.rest.crud.app.entities.User;


@Service
public interface UserService {

    List<User> findAll();
    
    Optional<User> findById( Long id );
    
    Optional<User> findByUsername( String username );
    
    User save ( User user );

    Optional<User> update( Long id, User user);

    Optional<User> delete( Long id );


}
