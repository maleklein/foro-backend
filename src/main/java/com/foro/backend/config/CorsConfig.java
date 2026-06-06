package com.foro.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

// @Configuration indica que esta clase define configuración de Spring
@Configuration
public class CorsConfig {

    // CORS (Cross-Origin Resource Sharing) es una política de seguridad del browser
    // que bloquea requests entre dominios distintos por defecto.
    // Como el BFF (puerto 4000) y el Frontend (puerto 3000) son "orígenes distintos"
    // al Backend (puerto 8080), necesitamos habilitarlo explícitamente.
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Solo estos dos orígenes pueden hacer requests al Backend:
        // - El BFF (Node.js) que actúa de intermediario
        // - El Frontend (Next.js) por si necesita llamar directamente
        config.setAllowedOrigins(List.of("http://localhost:4000", "http://localhost:3000"));

        // Métodos HTTP permitidos. OPTIONS es necesario para los "preflight requests"
        // que el browser hace antes de cada request cross-origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permitimos cualquier header en los requests (Authorization, Content-Type, etc.)
        config.setAllowedHeaders(List.of("*"));

        // Permite enviar cookies y credenciales en los requests cross-origin
        config.setAllowCredentials(true);

        // Aplicamos esta configuración a todas las rutas del Backend ("/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}