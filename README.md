# Todo Kanban Project

Monorepo para el proyecto Todo Kanban con arquitectura de microservicios.
Aplicación web Kanban To‑Do con stack de nivel enterprise: Angular (v21), Spring Boot 3.5.0 (Java 21), PostgreSQL y Docker Compose. 
Enfoque en arquitectura limpia (DDD/hexagonal), seguridad y una UX minimalista y elegante.

## Estructura del Proyecto

```
todo-kanban-project/
├── backend/          # Microservicios con Spring Boot 3.5.0 y Gradle
├── frontend/         # Aplicación web con Angular 21
└── infrastructure/   # Configuración de Docker, Traefik y Keycloak
```

## Tecnologías

### Backend
- **Framework**: Spring Boot 3.5.0
- **Build Tool**: Gradle
- **Java Version**: 21

### Frontend
- **Framework**: Angular 21
- **Package Manager**: npm
- **Styling**: SCSS
- **Routing**: Habilitado

### Infrastructure
- **Contenedorización**: Docker & Docker Compose
- **API Gateway**: Traefik
- **IAM**: Keycloak

## Primeros Pasos

### Backend
```bash
cd backend
./gradlew bootRun
```

### Frontend
```bash
cd frontend
npm start
```

## Estado del Proyecto

✅ Estructura inicial del monorepo creada
✅ Proyecto Spring Boot configurado
✅ Proyecto Angular configurado
⏳ Configuración de infrastructure pendiente
