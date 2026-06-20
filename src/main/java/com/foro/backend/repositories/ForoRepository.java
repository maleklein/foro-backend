package com.foro.backend.repositories;

import com.foro.backend.models.Foro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository marca esta interfaz como un componente de Spring.
// Spring Data JPA genera automáticamente la implementación en tiempo de ejecución.
@Repository
public interface ForoRepository extends JpaRepository<Foro, Long> {
    // JpaRepository<Foro, Long> hereda:
    //   findAll()       → trae todos los foros
    //   findById(id)    → trae un foro por id, devuelve Optional<Foro>
    //   save(foro)      → inserta o actualiza un foro
    //   deleteById(id)  → elimina un foro por id
    //   count()         → cantidad total de foros
    //   existsById(id)  → true si existe un foro con ese id

    // Query derivada del nombre: "SELECT * FROM foros WHERE faculty = ?"
    // Útil para mostrar foros filtrados por facultad (ej: todos los foros de "fci").
    List<Foro> findByFaculty(String faculty);
}
