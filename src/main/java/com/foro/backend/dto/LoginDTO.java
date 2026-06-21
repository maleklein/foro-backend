package com.foro.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
