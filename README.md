# Proyecto de Pruebas de Integración con Spring Boot

Proyecto para la asignatura **Infraestructuras y Procesos de Soporte** (UMA).  
Demuestra cómo estructurar y ejecutar pruebas unitarias y de integración en una API REST con Spring Boot.

---

## Estructura del proyecto

```
src/
├── main/java/com/uma/example/springuma/
│   ├── controller/
│   │   ├── PersonaController.java   # CRUD de personas
│   │   ├── CuentaController.java    # CRUD de cuentas bancarias
│   │   └── HelloController.java     # Endpoints de ejemplo iniciales
│   └── model/
│       ├── Persona.java             # Entidad JPA
│       ├── Cuenta.java              # Entidad JPA (ManyToOne → Persona)
│       ├── PersonaService.java      # Lógica de negocio Persona
│       ├── CuentaService.java       # Lógica de negocio Cuenta
│       ├── RepositoryPersona.java   # Spring Data JPA
│       └── RepositoryCuenta.java
└── test/java/com/uma/example/springuma/
    ├── unit/
    │   ├── controller/PersonaControllerTest.java   # Prueba unitaria con Mockito
    │   └── service/PersonaServiceTest.java         # Prueba unitaria parametrizada
    └── integration/
        ├── base/AbstractIntegration.java           # Clase base con configuración común
        ├── PersonaRepositoryIT.java                # Prueba de repositorio con @DataJpaTest
        ├── PersonaControllerMockMvcIT.java         # Prueba de integración con MockMvc
        └── PersonaControllerWebTestClientIT.java   # Prueba de integración con WebTestClient
```

---

## Requisitos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java        | 17            |
| Maven       | 3.8+          |

No necesitas instalar ninguna base de datos: el proyecto usa **H2 en memoria**.

---

## Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### Endpoints principales

| Método | Ruta              | Descripción                  |
|--------|-------------------|------------------------------|
| GET    | `/personas`       | Lista todas las personas     |
| GET    | `/persona/{id}`   | Obtiene una persona por ID   |
| POST   | `/persona`        | Crea una nueva persona       |
| PUT    | `/persona`        | Actualiza una persona        |
| DELETE | `/persona`        | Elimina una persona          |
| GET    | `/cuentas`        | Lista todas las cuentas      |
| POST   | `/cuenta`         | Crea una nueva cuenta        |

---

## Ejecutar las pruebas

```bash
# Pruebas unitarias
./mvnw test

# Pruebas de integración
./mvnw verify

# Todas las pruebas
./mvnw verify -Dsurefire.failIfNoSpecifiedTests=false
```

---

## Tipos de prueba incluidos

### Pruebas unitarias (`src/test/.../unit/`)
Usan **Mockito** para aislar la clase bajo prueba de sus dependencias.

- `PersonaServiceTest` — verifica que el servicio delega correctamente en el JpaRepository
- `PersonaControllerTest` — verifica que el controlador delega correctamente al servicio

### Pruebas de integración (`src/test/.../integration/`)
Arrancan el contexto de Spring completo (o una parte de él).

- `PersonaRepositoryIT` — usa `@DataJpaTest` para probar el repositorio con H2
- `PersonaControllerMockMvcIT` — usa `MockMvc` (sin servidor real)
- `PersonaControllerWebTestClientIT` — usa `WebTestClient` contra un servidor en puerto aleatorio

---

## Tecnologías

- Spring Boot 3.2
- Spring Data JPA + H2
- JUnit 5
- Mockito
- MockMvc
- WebTestClient (WebFlux)
