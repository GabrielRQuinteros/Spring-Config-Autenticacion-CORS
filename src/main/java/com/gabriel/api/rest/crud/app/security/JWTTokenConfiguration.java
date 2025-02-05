package com.gabriel.api.rest.crud.app.security;

import java.time.Duration;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;


public class JWTTokenConfiguration {

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    
    public static final String PREFIX_TOKEN = "Bearer ";
    
    public static final String HEADER_AUTHORIZATION = "Authorization";
    
    public static final Long EXPIRATION_TIME = Duration.ofMinutes(30).toMillis();

    public static final String JWT_CONTENT_TYPE = "application/json";


}
