package com.gabriel.api.rest.crud.app.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.api.rest.crud.app.entities.Role;
import com.gabriel.api.rest.crud.app.entities.User;
import com.gabriel.api.rest.crud.app.repositories.RoleRepository;
import com.gabriel.api.rest.crud.app.repositories.UserRepository;
import com.gabriel.api.rest.crud.app.services.interfaces.UserService;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    PasswordEncoder passwordEncoder;



    @Transactional( readOnly = true )
    @Override
    public List<User> findAll() {
        return ( List<User> ) this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return this.findById(id);
    }


    @Transactional
    @Override
    public User save(User user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleUserOpt = this.roleRepository.findByName("ROLE_USER");
        roleUserOpt.ifPresent( roles::add  );

        if( user.isAdmin() ) {
            Optional<Role> roleAdminOpt = this.roleRepository.findByName("ROLE_ADMIN");
            roleAdminOpt.ifPresent( roles::add  );
        }

        user.setRoles(roles);

        user.setPassword( this.passwordEncoder.encode(user.getPassword()) );

        return this.userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> update( Long id, User user) {

        Optional<User> userDBOpt = this.userRepository.findById(id);
        if ( userDBOpt.isEmpty() ) {
            return userDBOpt;
        }
        User userDB= userDBOpt.get();

        userDB.updateFromUser( user );

        User updatedUser=  this.userRepository.save(userDB);
        return Optional.of(updatedUser);
    }

    @Transactional
    @Override
    public Optional<User> delete( Long id ) {
        Optional<User> userDBOpt = this.userRepository.findById(id);
        if ( userDBOpt.isEmpty() ) {
            return userDBOpt;
        }
        this.userRepository.deleteById(id);
        return userDBOpt;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername( username );
    }

}
