package com.gabriel.api.rest.crud.app.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.api.rest.crud.app.repositories.UserRepository;


/* ¿¿ PARA QUE SE USA ESTA CLASE ??
 *  ---------------------------------
 *  
 */

@Service
public class JPAUserDetailsService implements UserDetailsService {

    @Autowired()
    private UserRepository userRepository;

    public JPAUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional( readOnly = true )
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        user.isEnabled(),
                        true,
                        true,
                        true,
                        user.getRoles().stream()
                                       .map( role -> new SimpleGrantedAuthority(role.getName()) )
                                       .toList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    /// NOTA IMPORTANTE:
    /// Spring usa por estandar roles con la estructura de nombre: ROLE_<algo>, ejemplo: ROLE_ADMIN.
    /// Spring asume internamente que vamos a seguir esta convención, y tiene métodos que "ESPERAN QUE SE USE ESTA CONCENCIÓN".
    /// Ejemplo:
    /// ----------------------------------
    /// Si usamos un rol ROLE_ADMIN:      |
    ///-----------------------------------
    ///  @PreAuthorize("hasRole('ADMIN')") // ✅ FUNCIONA
    ///    public void someAdminAction() { ... }
    /// ---------------------------------------
    /// Si "NO USAMOS LA CONVENCIÓN":          |
    /// ---------------------------------------
    /// @PreAuthorize("hasRole('ADMIN')") // ❌ NO FUNCIONA
    /// public void someAdminAction() { ... } 


}
