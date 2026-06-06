package com.foro.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// @RestController indica que esta clase es un Controller REST:
// combina @Controller + @ResponseBody, lo que significa que cada método
// devuelve datos directamente en el body del response (en formato JSON)
// en vez de renderizar una vista HTML
@RestController

// @RequestMapping define el prefijo de todas las rutas de este controller
// Todos los endpoints de esta clase van a empezar con /api
@RequestMapping("/api")
public class HealthController {

    // @GetMapping("/health") mapea los requests GET a /api/health a este método
    // ResponseEntity permite controlar el status HTTP de la respuesta (200, 404, etc.)
    // Map<String, String> es el body de la respuesta — se serializa automáticamente a JSON
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        // ResponseEntity.ok() devuelve un response con status 200
        // Map.of("status", "ok") crea el JSON { "status": "ok" }
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}