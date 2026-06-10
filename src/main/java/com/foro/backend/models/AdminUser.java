package com.foro.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

// @Entity: esta clase tiene representación en la BD (comparte la tabla "users" con StudentUser)
@Entity

// @DiscriminatorValue: cuando JPA guarda un AdminUser, pone "ADMIN" en la columna user_type
// Cuando lee una fila con user_type = "ADMIN", instancia un AdminUser automáticamente
@DiscriminatorValue("ADMIN")

// @Data: Lombok genera getters, setters, toString, equals y hashCode
@Data

// @EqualsAndHashCode(callSuper = true): incluye los campos de la clase padre (User y BaseEntity)
// en las comparaciones. Sin esto, dos AdminUser con el mismo id podrían verse como distintos.
@EqualsAndHashCode(callSuper = true)

@NoArgsConstructor
public class AdminUser extends User {

    // @Override indica que este método reemplaza al abstracto de User
    // POLIMORFISMO en acción: AdminUser tiene más permisos que StudentUser
    // porque es un tipo distinto de usuario con distinto comportamiento
    @Override
    public List<String> getPermissions() {
        return List.of(
            "CREATE_FORUM",      // puede crear foros
            "DELETE_FORUM",      // puede eliminar foros
            "EDIT_FORUM",        // puede editar foros
            "CREATE_POST",       // puede crear posts
            "DELETE_ANY_POST",   // puede borrar posts de cualquier usuario
            "BAN_USER"           // puede banear usuarios
        );
    }
}