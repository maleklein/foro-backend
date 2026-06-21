package com.foro.backend.services;

import com.foro.backend.dto.UserDTO;

public interface AuthService {
    UserDTO login(String email, String password);
}
