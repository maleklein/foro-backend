package com.foro.backend.controllers;

import com.foro.backend.dto.CrearForoDTO;
import com.foro.backend.dto.ForoDTO;
import com.foro.backend.services.ForosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Capa de presentación: recibe la request HTTP y delega toda la lógica al Service.
// El Controller no sabe nada de la BD — esa responsabilidad es del Repository.
// Esta separación en capas (Controller → Service → Repository) hace el código
// más fácil de testear y mantener.
@RestController
@RequestMapping("/api")
public class ForosController {

    // Dependemos de la interface, no de la implementación concreta.
    // Spring inyecta ForosServiceImpl automáticamente por ser el único @Service que la implementa.
    private final ForosService forosService;

    public ForosController(ForosService forosService) {
        this.forosService = forosService;
    }

    // GET /api/foros → devuelve la lista de foros con status 200
    // ResponseEntity<List<ForoDTO>> nos permite controlar el código HTTP de la respuesta
    @GetMapping("/foros")
    public ResponseEntity<List<ForoDTO>> listarForos() {
        // El Controller delega en el Service — no contiene lógica de negocio propia
        List<ForoDTO> foros = forosService.listarTodos();
        return ResponseEntity.ok(foros);
    }

    // POST /api/foros → crea un foro y devuelve 201 con el recurso creado
    // @Valid activa Bean Validation sobre el DTO: devuelve 400 si @NotBlank falla
    // @RequestBody deserializa el JSON del body al DTO automáticamente
    @PostMapping("/foros")
    public ResponseEntity<ForoDTO> crearForo(@Valid @RequestBody CrearForoDTO dto) {
        ForoDTO creado = forosService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
