# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

AI Track and Field is a Spring Boot web application for managing track and field competitions. The application uses:
- **Vaadin 24.9** for the UI framework
- **jOOQ** for database access with code generation from database schema
- **PostgreSQL** as the database
- **Flyway** for database migrations
- **Spring Boot 3.5** as the application framework

The application manages competitions, athletes, categories, events, and automatically calculates points based on IAAF ranking formulas.

## Development Commands

### Running the Application
```bash
./mvnw spring-boot:test-run
```
This runs the application with testcontainers support, automatically starting a PostgreSQL container.

### Building the Application
```bash
./mvnw clean install
```

### Code Generation
The build automatically handles jOOQ code generation in the `generate-sources` phase:
1. Groovy plugin starts a PostgreSQL Testcontainer
2. Flyway runs migrations against the container
3. jOOQ generates type-safe database access code into `ch.martinelli.demo.aitaf.db` package

To regenerate jOOQ classes after schema changes:
```bash
./mvnw clean generate-sources
```

### Running Tests
```bash
# All tests
./mvnw test

# KaribuTest unit tests only
./mvnw test -Dtest=*Test

# Playwright integration tests only
./mvnw test -Dtest=*IT
```

### Production Build
```bash
./mvnw clean package -Pproduction
```
This optimizes the Vaadin frontend bundle for production.

## Architecture

### Technology Stack Integration
- **jOOQ + Flyway**: Database schema is defined in Flyway migrations (`src/main/resources/db/migration`), and jOOQ generates type-safe DAOs from the schema during build
- **Vaadin + Spring**: Vaadin views are Spring-managed beans, using `@Route` annotations for navigation
- **Testcontainers**: Used both for development (via `spring-boot:test-run`) and testing, ensuring consistent PostgreSQL environments

### Package Structure
- `ch.martinelli.demo.aitaf` - Root package containing main application class
- `ch.martinelli.demo.aitaf.db` - Generated jOOQ classes (do not edit manually)
- Views, services, and repositories are organized under the root package

### Testing Architecture

**KaribuTest Base Class** (`src/test/java/.../KaribuTest.java`)
- Extends this for Vaadin component/view tests
- Uses Karibu-Testing to test Vaadin UIs without a browser
- Provides `login()` and `logout()` helper methods for security testing
- Uses `@ActiveProfiles("test")` and TestcontainersConfiguration
- Test data should be created using Flyway migrations in `src/test/resources/db/migration`
- NEVER use Mockito - use real Spring beans and database
- After tests, remove only the data created during the test, don't delete all data

**PlaywrightIT Base Class** (`src/test/java/.../PlaywrightIT.java`)
- Extends this for end-to-end browser tests
- Provides `page`, `mopo` (Vaadin-Playwright helper), and `localServerPort` fields
- Uses real PostgreSQL via Testcontainers
- Test data comes from the same Flyway test migrations as KaribuTest
- Set `launchOptions.headless = false` to see browser during development

### Database Management
- **Main migrations**: `src/main/resources/db/migration/V*.sql` (these don't exist yet - create as needed)
- **Test data migrations**: `src/test/resources/db/migration/V*.sql` (shared by KaribuTest and PlaywrightIT)
- Use sequences for primary keys (configured in jOOQ plugin with `ch.martinelli.oss.jooq.EqualsAndHashCodeJavaGenerator`)

## Development Workflow

This project follows an AI-assisted development workflow with slash commands in `.claude/commands/`:

1. **/1_requirements** - Create/update `docs/requirements.md` from `docs/vision.md` with functional/non-functional requirements as tables
2. **/2_entity_model** - Create `docs/entity_model.md` with Mermaid ERD diagram
3. **/3_use_case_diagram** - Create/update `docs/use_cases.puml` PlantUML diagram with UC-XXX identifiers
4. **/4_database_migration** - Create Flyway migrations from entity model (use sequences for PKs)
5. **/5_use_case_spec** - Create/update detailed use case specifications in `docs/use_cases/`
6. **/6_implement** - Implement a specific use case (check Vaadin and jOOQ MCP servers for guidance)
7. **/7_karibu_test** - Create KaribuTest for a use case (NEVER use Mockito, use Flyway for test data)
8. **/8_playwright_test** - Create Playwright test extending PlaywrightIT

## Important Implementation Guidelines

### Testing Requirements
- **NEVER use Mockito** - this is explicitly prohibited in this codebase
- Use real Spring beans and database interactions in tests
- Create test data using Flyway migrations in `src/test/resources/db/migration`
- Don't access service, repository, or DSLContext directly in tests to create data
- Clean up only the data created during tests, don't delete all data
- Use AssertJ assertions

### jOOQ Usage
- Generated jOOQ classes are in `ch.martinelli.demo.aitaf.db` package
- Access database via Spring's DSLContext bean
- Consult jOOQ MCP server for best practices

### Vaadin Development
- Views use `@Route` annotation for navigation
- Consult Vaadin MCP server for component usage and best practices
- The application uses Vaadin Spring Boot integration

### Code Generation
- Never manually edit files in `ch.martinelli.demo.aitaf.db` package
- After creating/modifying Flyway migrations, regenerate jOOQ code with `./mvnw generate-sources`
- The EqualsAndHashCodeJavaGenerator adds proper equals/hashCode to generated records
