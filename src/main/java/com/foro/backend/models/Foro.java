package com.foro.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// @Entity: esta clase tiene su propia tabla en la BD
@Entity

// @Table: define el nombre de la tabla en MySQL como "foros"
@Table(name = "foros")

// @Data: Lombok genera getters, setters, toString, equals y hashCode
@Data

// @EqualsAndHashCode(callSuper = true): incluye los campos de BaseEntity (id, createdAt, updatedAt)
// en las comparaciones de igualdad
@EqualsAndHashCode(callSuper = true)

// @NoArgsConstructor: constructor vacío que necesita JPA para instanciar los objetos
@NoArgsConstructor

// HERENCIA: Foro extiende BaseEntity y hereda id, createdAt, updatedAt
// sin necesidad de declararlos acá — BaseEntity ya los tiene
public class Foro extends BaseEntity {

    // nullable = false: todo foro debe tener nombre obligatoriamente
    @Column(nullable = false)
    private String name;

    // columnDefinition = "TEXT": permite descripciones largas
    // A diferencia de VARCHAR (255 chars máximo), TEXT puede guardar hasta 65,535 caracteres
    // nullable = true implícito: la descripción es opcional
    @Column(columnDefinition = "TEXT")
    private String description;

    // nullable = false: todo foro debe pertenecer a una facultad
    // Ejemplos de valores: "fci", "fce", "fcs", "General"
    @Column(nullable = false)
    private String faculty;
}