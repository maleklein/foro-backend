package com.foro.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// @Entity le dice a JPA que esta clase tiene su propia tabla en la BD
@Entity

// @Table define el nombre exacto de la tabla en MySQL: "users"
@Table(name = "users")

// Estrategia SINGLE_TABLE: AdminUser y StudentUser se guardan en la MISMA tabla "users"
// En vez de tener 3 tablas separadas, hay una sola con una columna extra "user_type"
// que dice si el registro es un ADMIN o un STUDENT
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

// Define la columna "user_type" que JPA usa para saber qué tipo de usuario es cada fila
// Ejemplo: si user_type = "ADMIN" → instancia AdminUser, si user_type = "STUDENT" → instancia StudentUser
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)

@Getter
@Setter
@NoArgsConstructor

// abstract: no se puede crear un User directamente, solo AdminUser o StudentUser
// Hereda id, createdAt, updatedAt de BaseEntity
public abstract class User extends BaseEntity {

    // unique = true: no pueden existir dos usuarios con el mismo email
    // nullable = false: el email es obligatorio
    @Column(nullable = false, unique = true)
    private String email;

    // Guardamos el HASH de la contraseña, nunca la contraseña en texto plano
    // bcrypt convierte "miPassword123" en algo como "$2b$10$xyz..." que no se puede revertir
    @Column(nullable = false)
    private String passwordHash;

    // unique = true: no pueden existir dos usuarios con el mismo nombre de usuario
    @Column(nullable = false, unique = true)
    private String username;

    // Rol general: "ADMIN" o "STUDENT"
    // Se usa para verificaciones rápidas sin necesidad de instanceof
    @Column(nullable = false)
    private String role;

    // POLIMORFISMO: este método es abstracto, lo que obliga a cada subclase
    // a definir su propia implementación.
    // AdminUser devuelve permisos de admin, StudentUser devuelve permisos de estudiante.
    // La ventaja es que cualquier código que reciba un User puede llamar
    // user.getPermissions() sin saber si es admin o estudiante — Java decide solo.
    public abstract List<String> getPermissions();
}