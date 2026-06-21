package com.foro.backend.services;

import com.foro.backend.dto.ForoDTO;
import com.foro.backend.models.Foro;
import com.foro.backend.repositories.ForoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service marca esta clase como componente de la capa de negocio.
// Spring la registra en el contexto y la inyecta donde se necesite ForosService.
// Al implementar la interface, cualquier otro servicio podría reemplazarla sin
// cambiar el Controller — eso es polimorfismo aplicado a la arquitectura en capas.
@Service
public class ForosServiceImpl implements ForosService {

    // Capa de acceso a datos: el Repository es inyectado por Spring (inversión de control).
    // El Service nunca habla directamente con la BD; delega en el Repository.
    private final ForoRepository foroRepository;

    public ForosServiceImpl(ForoRepository foroRepository) {
        this.foroRepository = foroRepository;
    }

    // Capa de negocio: orquesta la consulta y el mapeo.
    // findAll() viene heredado de JpaRepository — no hace falta escribir SQL.
    @Override
    public List<ForoDTO> listarTodos() {
        List<Foro> foros = foroRepository.findAll();

        // Mapeamos cada entidad JPA a un DTO antes de devolverlo al Controller.
        // Así la entidad nunca sale de la capa de servicio y el JSON no expone
        // campos internos ni provoca problemas de serialización con Hibernate.
        return foros.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Método privado de mapeo: traduce una entidad Foro a ForoDTO.
    // Centralizar el mapeo acá facilita cambiar los campos expuestos sin tocar el Controller.
    private ForoDTO mapToDTO(Foro foro) {
        return new ForoDTO(
                foro.getId(),
                foro.getNombre(),
                foro.getDescripcion(),
                foro.getFacultad(),
                foro.getCreatedAt(),
                foro.getUpdatedAt()
        );
    }
}
