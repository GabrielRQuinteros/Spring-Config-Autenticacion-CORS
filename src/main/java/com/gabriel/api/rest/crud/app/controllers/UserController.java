package com.gabriel.api.rest.crud.app.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.api.rest.crud.app.entities.User;
import com.gabriel.api.rest.crud.app.services.interfaces.UserService;

import jakarta.validation.Valid;


/** Cross Origins permite configurar los Cors de un controller en particular.
 * origins --> Una ruta en particular.
 * originPatterns --> Rutas que cumplan un cierto patron. 
 * 
 */
@CrossOrigin( origins = "http://localhost:4200", originPatterns = "*" )
@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired()
    private UserService userService;


    @GetMapping("")
    public ResponseEntity<?> findAll () {
        return ResponseEntity.ok().body(this.userService.findAll());
    }

    @PreAuthorize( "hasRole('ADMIN')" )
    @PostMapping("")
    public ResponseEntity<?> create (@Valid @RequestBody User userRequest, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            return validate( validationResult );
        }
        User newUser = this.userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@Valid @RequestBody User userRequest, BindingResult validationResult) {
        if( userRequest.isAdmin() )
            return ResponseEntity.badRequest().build();
        return create(userRequest, validationResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update ( @Valid @RequestBody User userRequest, BindingResult validationResult,  @PathVariable Long id) {
        if (validationResult.hasErrors()) {
            return validate( validationResult );
        }
        Optional<User> updatedUserOpt = this.userService.update( id, userRequest);
        if( updatedUserOpt.isPresent() )
            return ResponseEntity.ok().body(updatedUserOpt.get());
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Long id) {
        Optional<User> userOpt = this.userService.findById(id);
        if( userOpt.isPresent() )
            return ResponseEntity.ok().body(userOpt.get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete ( @PathVariable Long id ) {
        Optional<User> userOpt = this.userService.delete(id);
        if( userOpt.isPresent() )
            return ResponseEntity.ok(userOpt.get());
        else
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
