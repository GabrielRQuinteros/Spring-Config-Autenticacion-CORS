package com.gabriel.api.rest.crud.app.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.api.rest.crud.app.entities.Role;
import com.gabriel.api.rest.crud.app.repositories.RoleRepository;
import com.gabriel.api.rest.crud.app.services.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Transactional( readOnly = true )
    @Override
    public List<Role> findAll() {
        return ( List<Role> ) this.roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> findById(Long id) {
        return this.findById(id);
    }


    @Transactional
    @Override
    public Role save(Role role) {
       return this.roleRepository.save(role);
    }

    @Transactional
    @Override
    public Optional<Role> update( Long id, Role role) {

        Optional<Role> roleDBOpt = this.roleRepository.findById(id);
        if ( roleDBOpt.isEmpty() ) {
            return roleDBOpt;
        }
        Role roleDB= roleDBOpt.get();

        roleDB.updateFromRole( role );

        Role updatedRole=  this.roleRepository.save(roleDB);
        return Optional.of(updatedRole);
    }

    @Transactional
    @Override
    public Optional<Role> delete( Long id ) {
        Optional<Role> roleDBOpt = this.roleRepository.findById(id);
        if ( roleDBOpt.isEmpty() ) {
            return roleDBOpt;
        }
        this.roleRepository.deleteById(id);
        return roleDBOpt;
    }

}
