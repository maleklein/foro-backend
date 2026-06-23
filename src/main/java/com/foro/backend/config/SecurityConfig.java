package com.foro.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration indica que esta clase define beans de Spring (objetos que Spring gestiona)
// @EnableWebSecurity activa la configuración de seguridad web de Spring Security
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Bean le dice a Spring que este método crea un objeto que él va a gestionar e inyectar
    // SecurityFilterChain define las reglas de seguridad para los requests HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitamos CSRF porque nuestra API es stateless (no usa sesiones ni cookies de servidor)
            // En APIs REST consumidas desde otros servicios, CSRF no es necesario
            .csrf(csrf -> csrf.disable())

            // STATELESS significa que el servidor no guarda ninguna sesión entre requests
            // Cada request debe autenticarse por sí solo (con token, por ejemplo)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // El health check es público: cualquiera puede llamarlo sin autenticarse
                // Sirve para verificar que el servidor está corriendo
                .requestMatchers("/api/health").permitAll()

                .requestMatchers("/api/foros").permitAll()

                // El login debe ser público — es el punto de entrada de autenticación
                .requestMatchers("/api/auth/login").permitAll()

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // Bean de BCryptPasswordEncoder: permite encriptar y verificar contraseñas
    // BCrypt es un algoritmo de hashing one-way (no se puede revertir) que agrega
    // un "salt" aleatorio automáticamente, lo que lo hace muy seguro
    // Este bean se inyecta en los Services que necesiten encriptar passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}