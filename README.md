# Sistema de turnos y reservas — Backend

API REST para gestión de turnos en negocios como peluquerías, consultorios médicos y similares. Permite registrar clientes, profesionales y servicios, y gestionar reservas con control de estados.

**Demo en vivo:** [reservas-frontend-two.vercel.app](https://reservas-frontend-two.vercel.app)  
Para explorar como admin: usuario `admin@turnosapp.com` / contraseña `admin123`

**Repositorio frontend:** [github.com/serranoleon055/reservas-frontend](https://github.com/serranoleon055/reservas-frontend)

---

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot 3.4.5 | Framework web |
| Spring Security + JWT | Autenticación y autorización por roles |
| Spring Data JPA / Hibernate | Persistencia |
| MySQL 8 | Base de datos |
| JUnit 5 + Mockito | Tests unitarios |
| Maven | Gestión de dependencias |
| Railway | Deploy en producción |

---

## Características principales

- Autenticación JWT con roles `ADMIN` y `CLIENTE`
- Arquitectura en capas: Controller → Service → Repository, con DTOs y Mappers
- Validaciones con Bean Validation (`@Valid`)
- Manejo global de excepciones (`@ControllerAdvice`)
- Flujo de estados de turno: `PENDIENTE` → `CONFIRMADO` → `COMPLETADO` / `CANCELADO`
- Tests unitarios sobre la capa de Service con JUnit 5 y Mockito
- Deploy en producción con variables de entorno y CI/CD desde GitHub

---

## Estructura del proyecto

```
src/
├── controller/       # Endpoints REST
├── service/          # Lógica de negocio
├── repository/       # Acceso a datos (JPA)
├── model/            # Entidades JPA
├── dto/              # Objetos de transferencia de datos
├── mapper/           # Conversión entidad ↔ DTO
├── security/         # Configuración JWT y Spring Security
└── exception/        # Manejo global de errores
```

---

## Entidades

| Entidad | Descripción |
|---|---|
| `Usuario` | Autenticación y control de roles (ADMIN / CLIENTE) |
| `Cliente` | Persona que reserva turnos |
| `Profesional` | Prestador del servicio |
| `Servicio` | Tipo de servicio ofrecido (nombre, duración) |
| `Turno` | Reserva concreta entre cliente, profesional y servicio |

---

## Endpoints principales

### Autenticación
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/api/auth/registro` | Registrar nuevo usuario | No |
| POST | `/api/auth/login` | Login — devuelve JWT | No |

### Turnos
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/turnos` | Listar todos los turnos | Autenticado |
| POST | `/api/turnos` | Crear nuevo turno | Autenticado |
| PUT | `/api/turnos/{id}/estado` | Cambiar estado del turno | Autenticado |
| DELETE | `/api/turnos/{id}` | Eliminar turno | Autenticado |

### Clientes / Profesionales / Servicios
CRUD completo en `/api/clientes`, `/api/profesionales` y `/api/servicios`.  
Operaciones de escritura y eliminación requieren rol `ADMIN`.

---

## Cómo correr el proyecto localmente

**Requisitos:** Java 21+, MySQL 8, Maven

```bash
# 1. Clonar el repositorio
git clone https://github.com/serranoleon055/sistema-turnos.git
cd sistema-turnos

# 2. Crear base de datos
# En MySQL: CREATE DATABASE turnos_db;

# 3. Configurar credenciales en src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/turnos_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA

# 4. Correr la aplicación
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

---

## Tests

```bash
./mvnw test
```

Tests unitarios sobre `TurnoService` con JUnit 5 y Mockito, cubriendo creación, cambio de estado y casos de error.

---

## Autor

**León Serrano** — Desarrollador Fullstack (Java + React)  
[LinkedIn](https://www.linkedin.com/in/leonserrano/) · [GitHub](https://github.com/serranoleon055)
