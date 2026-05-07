# Sistema de Turnos y Reservas

## Descripción

Sistema backend para la gestión de turnos de negocios como peluquerias, consultorios medicos, etc. Permite a los clientes reservar turnos con profesionales
para servicios específicos, y a los administradores gestionar todo el sistema.

Desarrollado como proyecto personal para practicar conocimientos en back end buscando solucionar un problema real.

## Tecnologías

- Java 21
- Spring Boot 3.4.5
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL
- Maven
- JUnit 5 + Mockito

## Arquitectura

El proyecto sigue una arquitectura en capas:

- **Controller** — recibe las requests HTTP y devuelve responses
- **Service** — contiene la lógica de negocio
- **Repository** — accede a la base de datos
- **DTO** — separa la capa de presentación del modelo de datos
- **Mapper** — convierte entre entidades y DTOs

## Entidades principales

- **Usuario** — autenticación y roles (ADMIN / CLIENTE)
- **Cliente** — datos del cliente del negocio
- **Profesional** — prestador del servicio
- **Servicio** — lo que se puede reservar (nombre, descripción, duración)
- **Turno** — reserva concreta con estado (PENDIENTE, CONFIRMADO, CANCELADO, COMPLETADO)

## Requisitos para correr el proyecto

- Java 21 o superior
- MySQL 8
- Maven

## Configuración

1. Crear una base de datos MySQL llamada `turnos_db`
2. Configurar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/turnos_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

3. Correr la aplicación:

```bash
./mvnw spring-boot:run
```

## Autenticación

El sistema usa JWT. Para acceder a los endpoints protegidos:

1. Registrarse en `POST /api/auth/registro`
2. Hacer login en `POST /api/auth/login` — devuelve un token
3. Incluir el token en cada request: Authorization: Bearer <token>

## Endpoints

### Autenticación

| Método | Ruta               | Descripción             | Requiere Auth |
| ------ | ------------------ | ----------------------- | ------------- |
| POST   | /api/auth/registro | Registrar nuevo usuario | No            |
| POST   | /api/auth/login    | Iniciar sesión          | No            |

### Clientes

| Método | Ruta               | Descripción               | Requiere Auth |
| ------ | ------------------ | ------------------------- | ------------- |
| GET    | /api/clientes      | Listar todos los clientes | ADMIN         |
| GET    | /api/clientes/{id} | Obtener cliente por ID    | ADMIN         |
| POST   | /api/clientes      | Crear cliente             | ADMIN         |
| PUT    | /api/clientes/{id} | Actualizar cliente        | ADMIN         |
| DELETE | /api/clientes/{id} | Eliminar cliente          | ADMIN         |

### Profesionales

| Método | Ruta                    | Descripción                | Requiere Auth |
| ------ | ----------------------- | -------------------------- | ------------- |
| GET    | /api/profesionales      | Listar profesionales       | No            |
| GET    | /api/profesionales/{id} | Obtener profesional por ID | No            |
| POST   | /api/profesionales      | Crear profesional          | ADMIN         |
| PUT    | /api/profesionales/{id} | Actualizar profesional     | ADMIN         |
| DELETE | /api/profesionales/{id} | Eliminar profesional       | ADMIN         |

### Servicios

| Método | Ruta                | Descripción             | Requiere Auth |
| ------ | ------------------- | ----------------------- | ------------- |
| GET    | /api/servicios      | Listar servicios        | No            |
| GET    | /api/servicios/{id} | Obtener servicio por ID | No            |
| POST   | /api/servicios      | Crear servicio          | ADMIN         |
| PUT    | /api/servicios/{id} | Actualizar servicio     | ADMIN         |
| DELETE | /api/servicios/{id} | Eliminar servicio       | ADMIN         |

### Turnos

| Método | Ruta                    | Descripción              | Requiere Auth |
| ------ | ----------------------- | ------------------------ | ------------- |
| GET    | /api/turnos             | Listar todos los turnos  | Autenticado   |
| GET    | /api/turnos/{id}        | Obtener turno por ID     | Autenticado   |
| POST   | /api/turnos             | Crear turno              | Autenticado   |
| PUT    | /api/turnos/{id}/estado | Cambiar estado del turno | Autenticado   |
| DELETE | /api/turnos/{id}        | Eliminar turno           | Autenticado   |

### Usuarios

| Método | Ruta               | Descripción            | Requiere Auth |
| ------ | ------------------ | ---------------------- | ------------- |
| GET    | /api/usuarios      | Listar usuarios        | ADMIN         |
| GET    | /api/usuarios/{id} | Obtener usuario por ID | ADMIN         |
| POST   | /api/usuarios      | Crear usuario          | ADMIN         |

## Tests

El proyecto incluye tests unitarios sobre la capa de service con JUnit 5 y Mockito,
cubriendo los casos principales de lógica de negocio de TurnoService.

## Autor

Leon Serrano — linkedin.com/in/leonserrano/ — https://github.com/serranoleon055
