package com.foro.backend.models;

// Importamos las anotaciones de JPA (Jakarta Persistence API)
// que le dicen a Hibernate cómo mapear esta clase a la base de datos
import jakarta.persistence.*;

// Lombok: genera getters, setters y constructor vacío automáticamente
// sin tener que escribirlos a mano
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Anotaciones de Hibernate para manejar fechas automáticamente
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// @MappedSuperclass le dice a JPA que esta clase NO tiene su propia tabla en la BD.
// Sus campos (id, createdAt, updatedAt) se heredan y se incluyen en las tablas
// de las clases hijas (User, Foro). Es la base de nuestra jerarquía de herencia.
@MappedSuperclass

// Lombok genera todos los getters (getId(), getCreatedAt(), etc.)
@Getter

// Lombok genera todos los setters (setId(), setCreatedAt(), etc.)
@Setter

// Lombok genera un constructor vacío: new BaseEntity()
// JPA lo necesita para poder instanciar los objetos al leerlos de la BD
@NoArgsConstructor

// abstract significa que no se puede instanciar directamente: new BaseEntity() está prohibido.
// Solo se puede usar como clase base para User y Foro.
public abstract class BaseEntity {

    // @Id le dice a JPA que este campo es la clave primaria de la tabla
    @Id
    // @GeneratedValue hace que el id se genere automáticamente (1, 2, 3...)
    // IDENTITY significa que lo genera la BD (AUTO_INCREMENT en MySQL)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @CreationTimestamp hace que Hibernate asigne automáticamente
    // la fecha y hora actual cuando se crea el registro
    @CreationTimestamp
    // updatable = false significa que una vez asignado, nunca se modifica
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // @UpdateTimestamp hace que Hibernate actualice automáticamente
    // este campo cada vez que se modifica el registro
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}