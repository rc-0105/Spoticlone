# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

SpotiClone Backend — Universidad El Bosque, Bases de Datos 2 (2026-1). Spring Boot 3.2 REST API using **pure JDBC** (no JPA/Hibernate), backed by **PostgreSQL** and **MongoDB**.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=JwtServiceTest

# Run DB migrations only (standalone)
./mvnw exec:java -Dexec.mainClass=unbosque.edu.co.Spoticlone.migrate.Migrator
```

DB credentials for local dev are pre-configured in `.mvn/jvm.config` (passed as `-D` JVM args). The app reads `DB_URL`, `DB_USER`, `DB_PASSWORD` from system properties or env vars at startup and runs migrations before the Spring context starts.

## Architecture

### Dual-database design

| Concern | Technology | Access Layer |
|---|---|---|
| Relational data (users, songs, playlists, subscriptions) | PostgreSQL | `JdbcTemplate` + `DataSource.getConnection()` for stored procedures |
| Playback events (reproducciones) | MongoDB | Native `mongodb-driver-sync` (no Spring Data) |

JPA/Hibernate are **explicitly excluded** from Spring Boot autoconfiguration in `application.properties`. Every query is plain SQL.

### Package structure

```
config/         # PostgreSQLConfig (JDBC), MongoConfig (native client), SecurityConfig (BCrypt), FilterConfig (JWT filter + CORS)
controller/     # AuthController, CatalogController, PlaylistController, SuscripcionController, ReproduccionController, UsuarioController
dto/
  request/      # Input DTOs validated with @Valid
  response/     # Output DTOs (never expose raw entities)
exception/      # BusinessException (custom), GlobalExceptionHandler (@RestControllerAdvice)
filter/         # JwtAuthenticationFilter (OncePerRequestFilter, runs before DispatcherServlet)
migrate/        # Migrator.java — custom migration runner; reads sorted .sql files from sql/
repository/
  pg/           # CatalogRepository, PlaylistRepository, SuscripcionRepository, UsuarioRepository
  mongo/        # ReproduccionRepository
service/        # AuthService, JwtService, CatalogService, PlaylistService, SuscripcionService, ReproduccionService, UsuarioService
```

### Authentication flow

`JwtAuthenticationFilter` intercepts all `/api/*` requests. Routes under `/api/auth/**` are bypassed. On valid token it sets `userEmail` as a request attribute — controllers read it via `request.getAttribute("userEmail")`. No Spring Security filter chain is used; `SecurityConfig` only exposes a `BCryptPasswordEncoder` bean.

### DB operations pattern

- **Simple SELECTs** → `JdbcTemplate.query(sql, rowMapper, params)`  
- **Stored procedures (CALL)** → raw `DataSource.getConnection()` + `CallableStatement` (autoCommit=true — stored procs do their own COMMIT internally)  
- **PostgreSQL functions** → `{ ? = call fn_name(?) }` via `CallableStatement` with `registerOutParameter`  
- **MongoDB** → `getCollection()` returns the collection from `MongoClient`; use `Aggregates`/`Filters`/`Sorts` from the driver API

### Error handling

`BusinessException(message, statusCode)` is the single error type thrown from service/repository layers. `GlobalExceptionHandler` maps it to HTTP 400/409. `NoSuchElementException` → 404. All other exceptions → sanitized 500 (no internal details leaked).

### Migrations

`Migrator.run()` executes `.sql` files from the `sql/` directory in alphabetical order. Files are tracked by name + SHA-256 checksum in `migration_history`. Re-executing a changed file is blocked with a warning (not an error). Current migration files: `sql/001_ddl_schema.sql`, `sql/002_dml_seed.sql`.

### CORS

Allowed origins (configured in `FilterConfig`): `localhost:3000`, `localhost:5173`, `localhost:4321`.

## Key constraints

- **No JPA entities** — there are no `@Entity` classes. Database schema is owned by the SQL migration files.
- **Stored procedures own business logic** — playlist creation, adding songs, subscription changes all go through `CALL sp_*` procedures that enforce constraints at the DB level.
- JWT secret must be at least 256 bits (32+ characters) for HMAC-SHA256. The value in `application.properties` is for dev only.
