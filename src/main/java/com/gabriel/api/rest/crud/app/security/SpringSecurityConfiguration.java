package com.gabriel.api.rest.crud.app.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.gabriel.api.rest.crud.app.security.filters.JWTAuthenticationFilter;
import com.gabriel.api.rest.crud.app.security.filters.JWTValidationFilter;

import jakarta.servlet.FilterRegistration;


@Configuration
@EnableMethodSecurity( prePostEnabled = true)//--> Habilita las anotaciones como las que hice
                      // en GFondos para verificar los métodos en los endpoints o métodos de servicio.
                      // osea los @PreAuthorize( "hasAnyRole('ADMIN', 'USER')" ) etc.
public class SpringSecurityConfiguration {


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http ) throws Exception {
        return http.authorizeHttpRequests((authz) -> authz
        .requestMatchers( HttpMethod.GET, "/api/users").permitAll() ///--> Hace publico todos los endpoints GET del controlador de usuarios.
        
        
        .requestMatchers( HttpMethod.POST, "/api/users/register").permitAll() ///--> Hace publico el método POST de la ruta.
        // .requestMatchers( HttpMethod.POST, "/api/users").hasRole("ADMIN") 
        // .requestMatchers( HttpMethod.POST, "/api/products").hasRole("ADMIN") 
        // .requestMatchers( HttpMethod.GET, "/api/products", "/api/products/{id}" ).hasAnyRole("ADMIN", "USER") 
        // .requestMatchers( HttpMethod.PUT, "/api/products/{id}").hasRole("ADMIN") 
        // .requestMatchers( HttpMethod.DELETE, "/api/products/{id}").hasRole("ADMIN") 
        
        
        
        .anyRequest().authenticated())                    ///--> Hace privado todos los otros controladores y endpoints de esos controladores


        
        /// ACA AGREGO EL FILTRO DE AUTENTICACIÓN PARA EL LOGEO Y ENTREGA DEL TOKEN JWT.
        .addFilter( new JWTAuthenticationFilter(this.authenticationManager()))
        
        // ACA AGREGAMOS UN FILTRO PARA VERIFICAR SI EL USUARIO TIENE TOKEN Y SI ES VALIDO
        .addFilter( new JWTValidationFilter(this.authenticationManager()))


        .csrf( config -> config.disable())                /// Deshabilita la protección contra Cross Site Request Forgery. Esta opcion solo se usa
                                                          /// En aplicaciones con autenticación basados en Sesion.
                                                          /// La que yo uso normalmente es la de basada en tokens con los JWT.
                                                          /// 
                                                          /// Esta opción se habilita --> En aplicaciones con SSR que usan autenticación basada en sesión.
        

        .cors(cors -> cors.configurationSource(corsConfigurationSource())) /// AGREGAMOS LA CONFIGURACIÓN DE CORS A LA CADENA DE FILTROS



        //-> Se setea que la aplicacion se va a manejar con autenticacion.
        // Apps con autenticación: 
        //                        1- Basadas en TOKENS --> Crean sesiones STATELESS
        //                        2- Basadas en Sesiones --> Crean sesiones STATELESS.
        //
        // STATELLESS --> El servidor no guarda el "estado de la sesion".
        // STATEFULL  --> El servidor guarda el "estado de la sesion".
        .sessionManagement( management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


        .build();
    }


    /*** ¿ QUE ES CORS ? Cross Origins Sharing
     * ----------------------------------------
     * Es un mecanismo de seguridad implementado en los navegadores para restringir o permitir solicitudes HTTP entre diferentes orígenes.
     *
     *  Por defecto, los navegadores bloquean las solicitudes que se hacen desde un dominio a otro diferente
     *  (por ejemplo, si el frontend está en http://frontend.com y el backend en http://api.backend.com).
     *  CORS permite que el backend especifique qué orígenes externos pueden acceder a sus recursos.
     *  
     * ¿ Como funciona CORS ?
     * -------------------------
     * Cuando una aplicación en el frontend intenta acceder a una API en otro dominio,
     * el navegador hace una verificación previa (preflight request) usando el método OPTIONS.
     * 
     * ( Esto lo veo en GFondos cuando trato de comunicar el Angular con la API externa del cliente 
     * Cuando se hace una petición parece que se hace X2. Y La primera petición es con el método Options. )  
     * 
     * Esto pregunta al servidor si permite la solicitud desde ese origen. Si el backend responde con los encabezados adecuados,
     * el navegador procede con la petición real.
     * 
     */

    /** CONFIGURACIÓN DE "CORS"
     * ----------------------------
     * 1- Configuro el CorsConfigurationSource. Esto me devuelve la configuración de CORS que queremos .
     * 2- Configuro el Filtro de CORS, con  la mayor prioridad posible de Bean.
     * 3- Agregamos la configuración de CORS al filtro de seguridad de mas arriba con 
     *     .cors(cors -> cors.configurationSource(corsConfigurationSource()))
     * 
     */

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns( Arrays.asList( "*") );
        config.setAllowedMethods( Arrays.asList( "GET", "POST", "PUT", "DELETE") );
        config.setAllowedHeaders( Arrays.asList( "Authorization", "Content-Type" ) );
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean corsFilterBean = new FilterRegistrationBean<CorsFilter>( new CorsFilter( corsConfigurationSource() ) );
        corsFilterBean.setOrder( Ordered.HIGHEST_PRECEDENCE );
        return corsFilterBean;
    }

}
