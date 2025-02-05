package com.gabriel.api.rest.crud.app.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Esta clase se usa como un MixIn para la deserialización de objetos SimpleGrantedAuthority con Jackson.
 * 
 * Jackson necesita un constructor adecuado para mapear los datos del JSON a objetos de SimpleGrantedAuthority,
 * pero esta clase no tiene un constructor sin parámetros y su único constructor requiere un String.
 * 
 * Para solucionar esto, usamos @JsonCreator junto con @JsonProperty("authority"), lo que indica a Jackson
 * que cuando encuentre un campo llamado "authority" en el JSON, debe pasarlo al constructor de SimpleGrantedAuthority.
 * 
 * Esto se usa en ObjectMapper mediante `addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)`,
 * lo que permite mapear correctamente estructuras JSON como:
 * 
 * [
 *   { "authority": "ROLE_ADMIN" },
 *   { "authority": "ROLE_USER" }
 * ]
 * 
 * Sin este MixIn, Jackson no podría deserializar estos valores en objetos SimpleGrantedAuthority correctamente.
 */
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator()
    SimpleGrantedAuthorityJsonCreator ( @JsonProperty("authority") String role ){}

}
