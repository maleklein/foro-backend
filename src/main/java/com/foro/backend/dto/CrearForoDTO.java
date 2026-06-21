package com.foro.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO de entrada para crear un foro.
// Separa los datos que recibimos del cliente de la entidad JPA interna.
@Data
@NoArgsConstructor
public class CrearForoDTO {

    // @NotBlank: falla si el campo es null, vacío o solo espacios en blanco.
    // Spring devuelve 400 automáticamente si la validación falla (con @Valid en el Controller).
    @NotBlank
    private String nombre;

    // La descripción es opcional — no lleva @NotBlank
    private String descripcion;

    @NotBlank
    private String facultad;
}
