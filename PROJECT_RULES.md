# TODO-Kanban Project Rules

## Architecture: Hexagonal Architecture (Ports & Adapters)

This project follows **Hexagonal Architecture** principles, also known as **Ports and Adapters**, to ensure a clean separation of concerns, testability, and maintainability.

### Core Principles

1. **Domain-Driven Design (DDD)**: The business logic resides in the domain layer and is independent of external frameworks and technologies.

2. **Dependency Inversion**: Dependencies point inward. The domain layer depends on nothing. Application and infrastructure layers depend on the domain.

3. **Ports and Adapters**:
   - **Ports**: Interfaces that define how the application communicates with the outside world (input) and external systems (output)
   - **Adapters**: Implementations of ports that connect to specific technologies (REST controllers, JPA repositories, etc.)

### Layer Structure

```
📦 Backend (Hexagonal Architecture)
├── 🔷 domain/          # Business logic and entities (Core)
│   ├── model/          # Domain entities and value objects
│   ├── port/           # Interfaces (ports)
│   │   ├── in/         # Input ports (use cases)
│   │   └── out/        # Output ports (repository interfaces)
│   └── service/        # Domain services (business rules)
├── 🔌 application/     # Application orchestration
│   └── usecase/        # Use case implementations
├── 🔧 infrastructure/  # External concerns (Adapters)
│   ├── adapter/
│   │   ├── in/         # Input adapters (REST controllers, GraphQL, etc.)
│   │   └── out/        # Output adapters (JPA, external APIs, etc.)
│   └── config/         # Framework configuration (Spring, Security, etc.)
└── 📋 shared/          # Cross-cutting concerns (exceptions, utils)
```

---

## Team Roles & Responsibilities

### 🏛️ Architect

**Responsibilities**:
- Define and maintain the hexagonal architecture boundaries
- Ensure adherence to DDD principles
- Design the overall system structure and module boundaries
- Define ports (interfaces) for communication between layers
- Review and approve architectural decisions
- Define bounded contexts and ubiquitous language

**Technologies & Skills**:
- Domain-Driven Design (DDD)
- Hexagonal Architecture (Ports & Adapters)
- SOLID principles
- System design patterns
- Event-driven architecture
- Spring Boot 4.x architecture
- Angular 21 architecture patterns

**Key Deliverables**:
- Architecture Decision Records (ADRs)
- Domain model diagrams
- Bounded context maps
- Interface definitions (ports)
- Technical standards documentation

---

### ☕ Backend Developer

**Responsibilities**:
- Implement domain logic, use cases, and business rules
- Create domain entities and value objects following DDD principles
- Implement input ports (use case interfaces) and output ports (repository interfaces)
- Develop adapters for input (REST controllers) and output (JPA repositories, external APIs)
- Write unit tests for domain logic and integration tests for adapters
- Ensure Java 21 features are leveraged appropriately (records, sealed classes, pattern matching, virtual threads)
- Implement security with Keycloak OAuth2/OIDC integration

**Technologies**:
- **Java 21 LTS** (records, sealed classes, pattern matching, virtual threads, sequenced collections)
- **Spring Boot 4.0.x**
- **Spring Data JPA** (output adapters)
- **Spring Web** (input adapters - REST controllers)
- **Spring Security 7** (OAuth2 Resource Server with Keycloak)
- **PostgreSQL** (persistence)
- **Hibernate 7** (ORM)
- **JUnit 5** + **Mockito** (testing)
- **Gradle 8.x** (build tool)
- **Jakarta EE 11** (annotations and APIs)

**Key Deliverables**:
- Domain entities and aggregates
- Use case implementations
- REST API controllers (input adapters)
- JPA repositories (output adapters)
- Unit and integration tests
- API documentation (OpenAPI/Swagger)

**Code Example - Hexagonal Structure**:
```java
// Domain Layer - Entity
package com.todokanban.domain.model;

public class Task {
    private TaskId id;
    private String title;
    private TaskStatus status;
    
    // Business logic here
    public void moveToInProgress() {
        if (status == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot move completed task");
        }
        this.status = TaskStatus.IN_PROGRESS;
    }
}

// Domain Layer - Output Port
package com.todokanban.domain.port.out;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(TaskId id);
    List<Task> findAll();
}

// Domain Layer - Input Port (Use Case)
package com.todokanban.domain.port.in;

public interface CreateTaskUseCase {
    Task createTask(CreateTaskCommand command);
}

// Application Layer - Use Case Implementation
package com.todokanban.application.usecase;

@Service
public class CreateTaskService implements CreateTaskUseCase {
    private final TaskRepository taskRepository;
    
    @Override
    public Task createTask(CreateTaskCommand command) {
        Task task = Task.create(command.title(), command.description());
        return taskRepository.save(task);
    }
}

// Infrastructure Layer - Input Adapter (REST)
package com.todokanban.infrastructure.adapter.in.rest;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.createTask(request.toCommand());
        return ResponseEntity.ok(TaskResponse.from(task));
    }
}

// Infrastructure Layer - Output Adapter (JPA)
package com.todokanban.infrastructure.adapter.out.persistence;

@Repository
public class TaskJpaAdapter implements TaskRepository {
    private final TaskJpaRepository jpaRepository;
    
    @Override
    public Task save(Task task) {
        TaskEntity entity = TaskEntity.from(task);
        TaskEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }
}
```

---

### 🎨 Frontend Developer

**Responsibilities**:
- Implement Angular 21 standalone components with signals
- Create reactive user interfaces using RxJS and Angular Signals
- Implement client-side routing and lazy loading
- Integrate with backend REST APIs
- Implement authentication and authorization with Keycloak (OAuth2/OIDC)
- Ensure responsive design and accessibility (WCAG 2.1)
- Write unit tests (Jasmine/Jest) and E2E tests (Playwright/Cypress)
- Implement state management (NgRx Signals Store)

**Technologies**:
- **Angular 21** (standalone components, signals, control flow syntax)
- **TypeScript 5.7+**
- **RxJS 7.x** (reactive programming)
- **Angular Signals** (reactive state management)
- **NgRx Signals Store** (global state management)
- **Angular Material 21** or **PrimeNG** (UI components)
- **TailwindCSS** or **SCSS** (styling)
- **Keycloak Angular Adapter** (authentication)
- **Jasmine/Jest** (unit testing)
- **Playwright** or **Cypress** (E2E testing)
- **ESLint** + **Prettier** (code quality)

**Key Deliverables**:
- Angular standalone components
- Services for API communication
- Reactive forms with validation
- Authentication guards and interceptors
- Responsive layouts
- Unit and E2E tests
- Accessibility compliance

**Code Example - Angular 21 Component**:
```typescript
// Standalone component with signals
import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from './task.service';

@Component({
  selector: 'app-task-board',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="task-board">
      @for (task of tasks(); track task.id) {
        <div class="task-card">
          <h3>{{ task.title }}</h3>
          <p>{{ task.status }}</p>
        </div>
      } @empty {
        <p>No tasks available</p>
      }
    </div>
  `,
  styles: [`
    :host { display: block; }
  `]
})
export class TaskBoardComponent {
  private taskService = inject(TaskService);
  tasks = signal<Task[]>([]);

  ngOnInit() {
    this.taskService.getTasks().subscribe(tasks => {
      this.tasks.set(tasks);
    });
  }
}
```

---

### 🐳 DevOps Engineer

**Responsibilities**:
- Design and maintain Docker containerization strategy
- Configure orchestration with Docker Compose and Kubernetes (if needed)
- Set up Traefik reverse proxy for routing and load balancing
- Configure PostgreSQL database with persistence and backups
- Set up Keycloak for centralized authentication
- Implement CI/CD pipelines (GitHub Actions, GitLab CI, Jenkins)
- Monitor application health and performance (Prometheus, Grafana)
- Manage infrastructure as code (Terraform, Ansible)
- Ensure security best practices (secrets management, network policies)

**Technologies**:
- **Docker** & **Docker Compose** (containerization)
- **Traefik 3.x** (reverse proxy and load balancer)
- **PostgreSQL 16** (database)
- **Keycloak 26** (identity and access management)
- **Kubernetes** (container orchestration - optional)
- **GitHub Actions** / **GitLab CI** (CI/CD)
- **Prometheus** + **Grafana** (monitoring)
- **Terraform** (infrastructure as code)
- **Nginx** (alternative reverse proxy)
- **Let's Encrypt** (SSL/TLS certificates)

**Key Deliverables**:
- docker-compose.yml for local development
- Dockerfiles for backend and frontend
- CI/CD pipeline configurations
- Infrastructure as code scripts
- Monitoring and alerting setup
- Database backup and restore procedures
- Security hardening documentation

**Code Example - Multi-stage Dockerfile for Spring Boot**:
```dockerfile
# Backend Dockerfile (multi-stage build)
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Code Example - Angular Dockerfile**:
```dockerfile
# Frontend Dockerfile (multi-stage build)
FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

FROM nginx:alpine
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

## Development Guidelines

### Git Workflow
- **Feature branches**: `feature/task-creation`, `feature/user-authentication`
- **Bugfix branches**: `bugfix/task-deletion-error`
- **Commit messages**: Follow Conventional Commits (`feat:`, `fix:`, `chore:`, `docs:`)
- **Pull requests**: Require code review before merging

### Code Quality Standards
- **Backend**: Checkstyle, SpotBugs, SonarQube, JaCoCo (80% test coverage)
- **Frontend**: ESLint, Prettier, SonarQube, Karma/Jest (80% test coverage)
- **Documentation**: JavaDoc for public APIs, TSDoc for TypeScript

### Testing Strategy
- **Backend**: Unit tests (JUnit 5), Integration tests (Spring Boot Test), E2E tests (REST Assured)
- **Frontend**: Unit tests (Jest), Component tests (Angular Testing Library), E2E tests (Playwright)
- **Infrastructure**: Container health checks, smoke tests

---

## API Design Principles

- **RESTful conventions**: Use proper HTTP methods (GET, POST, PUT, PATCH, DELETE)
- **Versioning**: Use URI versioning (`/api/v1/tasks`)
- **Response formats**: JSON with consistent structure
- **Error handling**: Standardized error responses with codes and messages
- **Authentication**: JWT tokens from Keycloak (OAuth2/OIDC)
- **Authorization**: Role-based access control (RBAC)

---

## Security Requirements

- **Authentication**: OAuth2/OIDC via Keycloak
- **Authorization**: Role-based (USER, ADMIN)
- **HTTPS**: Mandatory in production
- **CORS**: Configured properly for frontend access
- **Input validation**: All user inputs validated and sanitized
- **SQL Injection prevention**: Use parameterized queries (JPA)
- **XSS prevention**: Angular built-in sanitization
- **CSRF protection**: Spring Security CSRF tokens

---

## Performance Requirements

- **API response time**: < 200ms for 95th percentile
- **Database queries**: Optimized with indexes and query plans
- **Frontend load time**: < 2 seconds initial load
- **Lazy loading**: Implement for Angular routes and modules
- **Caching**: Redis for frequently accessed data (future enhancement)

---

## Monitoring & Observability

- **Logging**: Structured logging (JSON format) with correlation IDs
- **Metrics**: Prometheus metrics for Spring Boot Actuator
- **Tracing**: Distributed tracing (OpenTelemetry - future)
- **Health checks**: `/actuator/health` for backend, custom checks for frontend
- **Dashboards**: Grafana dashboards for system overview

---

## Project Structure Summary

```
todo-kanban-project/
├── backend/                    # Spring Boot 4 + Java 21 + Hexagonal Architecture
│   ├── src/main/java/com/todokanban/
│   │   ├── domain/            # Domain layer (entities, ports, services)
│   │   ├── application/       # Application layer (use cases)
│   │   ├── infrastructure/    # Infrastructure layer (adapters, config)
│   │   └── shared/            # Shared utilities
│   └── build.gradle
├── frontend/                   # Angular 21 + TypeScript
│   ├── src/app/
│   │   ├── features/          # Feature modules (standalone components)
│   │   ├── core/              # Core services and guards
│   │   ├── shared/            # Shared components and utilities
│   │   └── app.config.ts      # Application configuration
│   └── package.json
├── infrastructure/             # Docker Compose, Kubernetes configs
│   ├── docker-compose.yml
│   ├── Dockerfile.backend
│   └── Dockerfile.frontend
└── PROJECT_RULES.md            # This file
```

---

## Access URLs (Local Development)

- **Frontend**: http://app.localhost
- **Backend API**: http://api.localhost
- **Keycloak**: http://keycloak.localhost (admin/admin)
- **Traefik Dashboard**: http://localhost:8080
- **PostgreSQL**: localhost:5432 (kanban_user/kanban_password)

---

**Last Updated**: 2026-02-09  
**Version**: 1.0.0
