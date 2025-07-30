# 🧮 Calculator API – Backend

This project is a RESTful API for a calculator with credit control per operation. Users can perform math operations (addition, subtraction, multiplication, division, square root, and random string generation), each consuming a credit amount and being logged.

## 🔧 Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security (JWT)
- PostgreSQL
- Flyway (migrations)
- Swagger / OpenAPI
- JUnit 5 / Mockito

---

## 📦 Package Structure (Clean Architecture)

```
 com.calculator
├── application         # Business logic (use cases)
├── domain              # Entities, enums, interfaces
├── infrastructure      # JPA repositories and configuration
├── presentation        # REST controllers
├── security            # JWT, filters, authentication
└── config              # Beans and global configuration
```

---

## 🚀 Running Locally

### Prerequisites

- Java 17
- Docker or local PostgreSQL
- Gradle
- (optional) Flyway CLI

### Run with Docker (PostgreSQL)

```bash
docker-compose up -d
```

### Application

```bash
./gradlew bootRun
```

API: `http://localhost:8080/api/v1/`

Swagger: `http://localhost:8080/swagger-ui.html`

---

## 🛠️ Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/calculator
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
jwt:
  secret: changeme-super-secret
```

---

## 🔑 Main Endpoints

| Method | Route                      | Description                   |
| ------ | -------------------------- | ----------------------------- |
| POST   | `/api/v1/auth/login`       | Login and get JWT token       |
| POST   | `/api/v1/operations`       | Execute an operation          |
| GET    | `/api/v1/operations/types` | List available operations     |
| GET    | `/api/v1/records`          | List user's operation records |
| DELETE | `/api/v1/records/{id}`     | Soft delete a record          |

---

## ✅ TODO: Backend Tasks

###  🧱 Infrastructure
 
- Base project with Spring Boot 3 and Gradle
- Configure PostgreSQL with Flyway
- Implement CORS and API versioning
- Configure Swagger with JWT security

### 🔐 Authentication

- User entity with status and encrypted password 
- Login with JWT 
- Middleware to validate the token and extract the user

### 🧮 Operation Logic

- OperationType entity with fixed types and cost 
- Record entity with soft delete
- Operation execution service:
  - Check balance before performing the operation 
  - Perform the operation (backend)
  - For random_string: integrate with [random.org](https://www.random.org/clients)
  - Update user balance 
  - Save record with current balance and result

### 📊 History and CRUD

- Paginated record listing endpoint (with filtering and sorting)
- Soft delete via endpoint

### ✅ Tests and Security

- Unit tests with JUnit 5 and Mockito
- Integration tests with Cucumber
- Input validation 
- Global exception handling 
- Security: protect all routes with JWT

---

## 📄 License

This project is free to use for demonstration and testing purposes.

