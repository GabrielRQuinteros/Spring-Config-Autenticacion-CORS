package com.gabriel.api.rest.crud.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.api.rest.crud.app.entities.Role;
import com.gabriel.api.rest.crud.app.services.interfaces.RoleService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/role")
public class RoleController {


    @Autowired()
    private RoleService roleService;


    @GetMapping("")
    public ResponseEntity<?> findAll () {
        return ResponseEntity.ok().body(this.roleService.findAll());
    }

    @PostMapping("")
    public ResponseEntity<?> create ( @Valid @RequestBody Role roleRequest, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            return validate( validationResult );
        }
        Role newRole = this.roleService.save(roleRequest);
        return ResponseEntity.ok().body(newRole);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update ( @Valid @RequestBody Role roleRequest, BindingResult validationResult, @PathVariable Long id) {
        if (validationResult.hasErrors()) {
            return validate( validationResult );
        }
        Optional<Role> updatedRoleOpt = this.roleService.update( id, roleRequest);
        if( updatedRoleOpt.isPresent() )
            return ResponseEntity.ok().body(updatedRoleOpt.get());
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById ( @PathVariable Long id ) {
        Optional<Role> roleOpt = this.roleService.findById(id);
        if( roleOpt.isPresent() )
            return ResponseEntity.ok().body(roleOpt.get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete ( @PathVariable Long id ) {
        Optional<Role> roleOpt = this.roleService.delete(id);
        if( roleOpt.isPresent() )
            return ResponseEntity.ok(roleOpt.get());
        else
            return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validate (BindingResult validationResult) {
        Map<String, String> errors = new HashMap<>();
        validationResult.getFieldErrors().forEach((err) -> {
            errors.put( err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);

    }
    

}
