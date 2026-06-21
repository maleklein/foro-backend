package com.foro.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO (Data Transfer Object): objeto plano que viaja entre capas y hacia el cliente.
// Nunca exponemos la entidad JPA directamente — el DTO controla qué campos
// son visibles y evita filtrar relaciones lazy o campos internos de Hibernate.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForoDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    // Facultad a la que pertenece el foro (ej: "fci", "fce", "General")
    private String facultad;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
