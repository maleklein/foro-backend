# Foro Backend

API REST construida con Spring Boot 3, Spring Data JPA y MySQL. Persiste los
datos del foro (usuarios y foros) en una base de datos relacional y expone
endpoints REST que consume el BFF.

## Rol en la arquitectura

El proyecto Foro UAP está dividido en tres servicios:

```
┌──────────────┐      ┌──────────────┐      ┌──────────────────┐
│   Frontend   │ ───▶ │     BFF      │ ───▶ │  Backend (este)  │
│  Next.js     │      │  Node + BFF  │      │  Spring Boot     │
│  :3000       │      │  :4000       │      │  :8080           │
└──────────────┘      └──────────────┘      └──────────────────┘
                              │                       │
                              ▼                       ▼
                       ┌──────────┐            ┌────────────┐
                       │ MongoDB  │            │   MySQL    │
                       │ (cache)  │            │ (relacional)│
                       └──────────┘            └────────────┘
```

El Backend es la **fuente de verdad** de los datos. El BFF lo consulta y mantiene
una caché en MongoDB para responder rápido al Frontend.

## Stack tecnológico

| Componente       | Versión          | Para qué                                      |
|------------------|------------------|-----------------------------------------------|
| Java             | 17               | Lenguaje                                      |
| Spring Boot      | 3.5.14           | Framework principal                           |
| Spring Data JPA  | (Boot starter)   | ORM con Hibernate                             |
| Spring Security  | (Boot starter)   | `BCryptPasswordEncoder` y filtros HTTP        |
| Hibernate        | (default de Boot)| Mapeo objeto-relacional                       |
| MySQL Connector  | runtime          | Driver JDBC para MySQL 8                      |
| Lombok           | compile-only     | Genera getters/setters/constructores          |
| Maven            | wrapper incluido | Build tool (no requiere instalación global)   |

## Requisitos previos

- **Java 17 o superior** ([Adoptium Temurin recomendado](https://adoptium.net/temurin/releases/?version=17))
- **MySQL 8+** instalado y corriendo localmente
- **Maven** — el proyecto incluye `mvnw` (wrapper), así que no hace falta
  instalarlo globalmente

## Instalación paso a paso

### 1. Clonar el repositorio

```bash
git clone git@github.com:maleklein/foro-backend.git
cd foro-backend
```

### 2. Crear la base de datos en MySQL

Antes de levantar el proyecto, creá la base manualmente desde el cliente de MySQL:

```sql
CREATE DATABASE foro_backend;
```

> Hibernate va a crear y actualizar las tablas automáticamente (gracias a
> `spring.jpa.hibernate.ddl-auto=update`), pero **la base de datos sí hay
> que crearla a mano**.

### 3. Configurar las credenciales

Si tu MySQL local usa `root` / `root`, no toques nada. Si no, editá
`src/main/resources/application.properties`:

```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### 4. Compilar e iniciar

```bash
./mvnw clean install
./mvnw spring-boot:run
```

El servidor levanta en `http://localhost:8080`.

> En Windows, usar `mvnw.cmd` en vez de `./mvnw`.

## Endpoints disponibles

| Método | Ruta          | Descripción                       | Auth requerida |
|--------|---------------|-----------------------------------|----------------|
| GET    | `/api/health` | Health check del servidor         | No             |

> El resto de endpoints (usuarios, foros, login) están definidos como tareas
> separadas en el board del equipo y se irán sumando.

### Ejemplo de uso

```bash
curl http://localhost:8080/api/health
# → { "status": "ok" }
```

## Estructura del proyecto

```
foro-backend/
├── pom.xml                                  # Configuración Maven
├── mvnw, mvnw.cmd                           # Wrapper de Maven (no requiere install)
├── src/
│   ├── main/
│   │   ├── java/com/foro/backend/
│   │   │   ├── BackendApplication.java      # Entry point (main)
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java          # CORS para que el BFF nos llame
│   │   │   │   └── SecurityConfig.java      # Spring Security + BCrypt
│   │   │   ├── controllers/
│   │   │   │   └── HealthController.java    # GET /api/health
│   │   │   └── models/                      # Entidades JPA (ver abajo)
│   │   │       ├── BaseEntity.java
│   │   │       ├── User.java                # abstracto
│   │   │       ├── AdminUser.java
│   │   │       ├── StudentUser.java
│   │   │       └── Foro.java
│   │   └── resources/
│   │       └── application.properties       # Configuración de Spring
│   └── test/
│       └── java/com/foro/backend/
│           └── BackendApplicationTests.java
```

## Modelos: herencia y polimorfismo

El modelo de dominio aprovecha herencia JPA para cumplir el requisito de la
materia. Resumen rápido:

### `BaseEntity` — clase base abstracta

`@MappedSuperclass`. No tiene su propia tabla. Aporta `id`, `createdAt` y
`updatedAt` a sus subclases (`User` y `Foro`).

### `User` — entidad abstracta con herencia `SINGLE_TABLE`

Hereda de `BaseEntity`. Usa estrategia `SINGLE_TABLE`: en MySQL existe una
sola tabla `users` con todos los usuarios, y una columna discriminadora
`user_type` que vale `"ADMIN"` o `"STUDENT"` según la subclase.

Define el método **abstracto** `getPermissions()`, que cada subclase
implementa con sus permisos propios — esto es polimorfismo: cualquier
código que reciba un `User` puede pedir `user.getPermissions()` sin saber
de qué tipo es.

Campos: `email`, `passwordHash`, `username`, `role`.

### `AdminUser` y `StudentUser` — subclases concretas

Cada una define su propia versión de `getPermissions()` (admin con
permisos de moderación; student con permisos básicos).

### `Foro`

Hereda `id` + timestamps de `BaseEntity`. Campos propios: `name`,
`description` (TEXT, opcional), `faculty` (obligatoria).

## Configuración

### CORS

Definida en `CorsConfig.java`. Permite requests desde el BFF en
`http://localhost:4000`.

### Spring Security

Definida en `SecurityConfig.java`:

- **Sessions: STATELESS** — el servidor no guarda sesión entre requests.
- **CSRF deshabilitado** — la API es stateless, no aplica.
- **`/api/health` público** — el resto requiere autenticación.
- **`PasswordEncoder` bean** — usa `BCryptPasswordEncoder`, que es one-way
  y agrega salt automático. Se inyecta en los services que necesitan
  encriptar passwords.

### JPA / Hibernate

- `ddl-auto=update`: Hibernate crea y actualiza las tablas automáticamente
  basándose en las anotaciones `@Entity`. En producción se recomienda usar
  migraciones explícitas (Flyway o Liquibase).
- `show-sql=true`: las consultas SQL aparecen en consola para debug.
- Dialecto: `MySQLDialect`.

## Variables de configuración

Definidas en `src/main/resources/application.properties`:

| Propiedad                                 | Valor por defecto                          |
|-------------------------------------------|--------------------------------------------|
| `server.port`                             | `8080`                                     |
| `spring.datasource.url`                   | `jdbc:mysql://localhost:3306/foro_backend` |
| `spring.datasource.username`              | `root`                                     |
| `spring.datasource.password`              | `root`                                     |
| `spring.jpa.hibernate.ddl-auto`           | `update`                                   |
| `spring.jpa.show-sql`                     | `true`                                     |
| `spring.jpa.properties.hibernate.dialect` | `org.hibernate.dialect.MySQLDialect`       |

## Troubleshooting

### "Communications link failure" al levantar

MySQL no está corriendo o no escucha en `localhost:3306`. Verificá con
`mysql -u root -p` que el servicio esté arriba.

### "Unknown database 'foro_backend'"

Olvidaste crear la base. Corré `CREATE DATABASE foro_backend;` antes de
`./mvnw spring-boot:run`.

### "Access denied for user 'root'@'localhost'"

Las credenciales en `application.properties` no coinciden con las de tu
MySQL local. Ajustá `spring.datasource.username` / `password`.

### Cambié una entidad y la tabla no se actualizó

`ddl-auto=update` agrega columnas y tablas, pero NO borra ni renombra
columnas existentes para no perder datos. Si necesitás un reseteo total,
podés cambiar temporalmente a `ddl-auto=create-drop` (borra todo al
levantar y al cerrar) o dropear la base y recrearla.

### El BFF dice "CORS error"

Verificá que `CorsConfig.java` esté permitiendo `http://localhost:4000`
(la URL exacta desde donde corre el BFF). Si el BFF corre en otro puerto,
hay que ajustar esa lista.

## Tests

```bash
./mvnw test
```

Por ahora solo está el test de contexto de Spring Boot
(`BackendApplicationTests`).
