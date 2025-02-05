package com.gabriel.api.rest.crud.app.security.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.api.rest.crud.app.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.gabriel.api.rest.crud.app.security.JWTTokenConfiguration.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    
        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
            
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(userPassAuthToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        org.springframework.security.core.userdetails.User userSpringSecurity = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        String username= userSpringSecurity.getUsername();
        Long currentTime = System.currentTimeMillis();
        
        Claims claimsRoles = Jwts.claims()
                                .add("authorities", new ObjectMapper().writeValueAsString(authResult.getAuthorities())) 
                                .build();


        String jwtToken= Jwts.builder().subject(username)
                                       .issuedAt(new Date(currentTime))
                                       .expiration(new Date( currentTime + EXPIRATION_TIME ))
                                       .claims( claimsRoles )
                                       .signWith(SECRET_KEY)
                                       .compact();

        response.addHeader( HEADER_AUTHORIZATION , PREFIX_TOKEN + jwtToken);

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("token", jwtToken);
        map.put("message", String.format("Usuario %s, se ha logeado con exito.", username));

        response.getWriter().write( new ObjectMapper().writeValueAsString(map) );
        response.setContentType(JWT_CONTENT_TYPE);
        response.setStatus(HttpStatus.OK.value());
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> map = new HashMap<>();
        map.put("message", "Error de autenticación. Usuario o contraseña incorrectos.");
        map.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(map));
        response.setContentType(JWT_CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }



    
    
    


}
