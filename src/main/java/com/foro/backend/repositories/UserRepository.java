package com.foro.backend.repositories;

import com.foro.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository marca esta interfaz como un componente de Spring.
// Spring Data JPA genera automáticamente la implementación en tiempo de ejecución —
// no hace falta escribir ninguna clase concreta.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<User, Long> hereda:
    //   findAll()       → trae todos los usuarios
    //   findById(id)    → trae un usuario por id, devuelve Optional<User>
    //   save(user)      → inserta o actualiza un usuario
    //   deleteById(id)  → elimina un usuario por id
    //   count()         → cantidad total de usuarios
    //   existsById(id)  → true si existe un usuario con ese id

    // Spring Data JPA genera la query SQL a partir del nombre del método:
    // "SELECT * FROM users WHERE email = ?"
    // Devuelve Optional<User> para forzar a quien lo llame a manejar el caso de que no exista.
    // Es el método clave para el login: buscar un usuario por su email.
    Optional<User> findByEmail(String email);
}
