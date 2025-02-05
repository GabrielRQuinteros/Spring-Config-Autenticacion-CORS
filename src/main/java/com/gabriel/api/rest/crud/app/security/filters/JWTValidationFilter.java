package com.gabriel.api.rest.crud.app.security.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.api.rest.crud.app.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.gabriel.api.rest.crud.app.security.JWTTokenConfiguration.*;

/** Esta clase define un Filtro que se usa para validar si la petición tiene el Token JWT. 
 * 
 */

public class JWTValidationFilter extends BasicAuthenticationFilter {


    public JWTValidationFilter (  AuthenticationManager authenticationManager ) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        String header = request.getHeader(HEADER_AUTHORIZATION);

        if(header == null || !header.startsWith(PREFIX_TOKEN) ) {
            chain.doFilter(request, response);
            return;
        }
        
        String token = header.replace(PREFIX_TOKEN, "");


        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Object authoritiesCLaims = claims.get( "authorities" );

            Collection<? extends GrantedAuthority> authorities = List.of(
                new ObjectMapper()
                /// Se usa ObjectMapper para convertir el JSON de authoritiesClaims en una lista de SimpleGrantedAuthority.
                // Como SimpleGrantedAuthority no tiene un constructor sin parámetros, y su constructor espera un String ("role"),
                // se usa un MixIn (SimpleGrantedAuthorityJsonCreator) para indicarle a Jackson cómo mapear correctamente los datos.
                // Esto es necesario porque el JSON original no usa la clave esperada ("authority"), sino otra distinta.
                .addMixIn( SimpleGrantedAuthority.class , SimpleGrantedAuthorityJsonCreator.class )
                .readValue(authoritiesCLaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token JWT es invalido.");
            response.getWriter().write( new ObjectMapper().writeValueAsString(body) );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
        }



    }

    

    
}
