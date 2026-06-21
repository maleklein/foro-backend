package com.foro.backend.controllers;

import com.foro.backend.dto.LoginDTO;
import com.foro.backend.dto.UserDTO;
import com.foro.backend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            UserDTO userDTO = authService.login(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            // Si el Service lanza excepción (usuario no existe o password incorrecta)
            // devolvemos 401 sin revelar cuál de los dos falló
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
