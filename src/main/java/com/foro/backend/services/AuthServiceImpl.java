package com.foro.backend.services;

import com.foro.backend.dto.UserDTO;
import com.foro.backend.models.User;
import com.foro.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO login(String email, String password) {
        // Buscamos el usuario por email. Si no existe, lanzamos la misma excepción
        // que cuando la password es incorrecta — así no damos pistas sobre qué falló
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // passwordEncoder.matches() compara la password ingresada con el hash guardado
        // BCrypt nunca "desencripta" — solo verifica que el hash coincide
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Devolvemos un DTO con solo los campos necesarios — nunca exponemos el passwordHash
        return new UserDTO(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }
}
