# Foro Backend

API REST construida con Spring Boot 3, Spring Data JPA y MySQL.

## Requisitos

- Java 17+
- Maven 3.8+
- MySQL 8+

## Configuración de la base de datos

Crear la base de datos antes de levantar el proyecto:

```sql
CREATE DATABASE foro_backend;
```

Editar las credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

## Levantar el proyecto

```bash
mvn clean install
mvn spring-boot:run
```

El servidor corre en `http://localhost:8080`.

## Health check

```
GET http://localhost:8080/api/health
```

Respuesta esperada:

```json
{ "status": "ok" }
```

## Estructura de paquetes

```
com.foro.backend/
  controllers/   — endpoints REST
  services/      — lógica de negocio
  repositories/  — acceso a datos (Spring Data JPA)
  models/        — entidades JPA
  dto/           — objetos de transferencia de datos
  config/        — configuración (CORS, Security, etc.)
```

## CORS

Configurado para aceptar requests desde `http://localhost:4000` (BFF).
