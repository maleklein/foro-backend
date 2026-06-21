package com.foro.backend.services;

import com.foro.backend.dto.CrearForoDTO;
import com.foro.backend.dto.ForoDTO;

import java.util.List;

// Interface de servicio: define el contrato de negocio sin acoplarse a ninguna
// implementación concreta. El Controller depende de esta abstracción, no de la clase.
// Esto es el principio de Inversión de Dependencias (SOLID).
public interface ForosService {

    // Devuelve todos los foros mapeados a DTO listos para serializar a JSON
    List<ForoDTO> listarTodos();

    // Crea un nuevo foro en la BD y devuelve el DTO con el id generado
    ForoDTO crear(CrearForoDTO dto);
}
