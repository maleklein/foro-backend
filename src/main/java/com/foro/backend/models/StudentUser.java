package com.foro.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

// @Entity: esta clase tiene representación en la BD (comparte la tabla "users" con AdminUser)
@Entity

// @DiscriminatorValue: cuando JPA guarda un StudentUser, pone "STUDENT" en la columna user_type
// Cuando lee una fila con user_type = "STUDENT", instancia un StudentUser automáticamente
@DiscriminatorValue("STUDENT")

// @Data: Lombok genera getters, setters, toString, equals y hashCode
@Data

// @EqualsAndHashCode(callSuper = true): incluye los campos de la clase padre (User y BaseEntity)
// en las comparaciones. Sin esto, dos StudentUser con el mismo id podrían verse como distintos.
@EqualsAndHashCode(callSuper = true)

@NoArgsConstructor
public class StudentUser extends User {

    // @Override indica que este método reemplaza al abstracto de User
    // POLIMORFISMO en acción: StudentUser define sus propios permisos,
    // más restringidos que los de AdminUser
    @Override
    public List<String> getPermissions() {
        return List.of(
            "VIEW_FORUM",      // puede ver foros
            "CREATE_POST",     // puede crear posts
            "EDIT_OWN_POST",   // solo puede editar sus propios posts
            "DELETE_OWN_POST"  // solo puede borrar sus propios posts
        );
    }
}