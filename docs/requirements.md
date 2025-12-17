# Requirements

## Functional Requirements

| ID | User Story | Priority | Status |
|----|------------|----------|--------|
| FR-001 | As an administrator, I want to create and manage competitions so that I can organize track and field events | High | Draft |
| FR-002 | As an administrator, I want to define categories with gender, year from, and year to so that athletes can be properly grouped | High | Draft |
| FR-003 | As an administrator, I want to associate events with categories so that I can define which competitions athletes in each category will participate in | High | Draft |
| FR-004 | As an administrator, I want to register athletes with their personal information (name, birth year, gender, club) so that they can participate in competitions | High | Draft |
| FR-005 | As a system, I want to automatically assign athletes to categories based on their birth year and gender so that categorization is consistent and error-free | High | Draft |
| FR-006 | As an administrator, I want to record athlete results for each event so that performance can be tracked | High | Draft |
| FR-007 | As a system, I want to calculate points for each athlete based on their event results using IAAF ranking formulas so that performance is standardized | High | Draft |
| FR-008 | As a system, I want to automatically rank athletes after all have completed their events so that standings are accurate | High | Draft |
| FR-009 | As a user, I want to view ranking lists grouped by category showing athlete names, event results, and total points so that I can see competition standings | High | Draft |
| FR-010 | As an administrator, I want to manage clubs so that athletes can be associated with their organizations | Medium | Draft |
| FR-011 | As an administrator, I want to edit competition details so that I can make corrections or updates | Medium | Draft |
| FR-012 | As an administrator, I want to edit athlete information so that I can correct registration errors | Medium | Draft |

## Non-Functional Requirements

| ID | Description | Priority | Status |
|----|-------------|----------|--------|
| NFR-001 | The system must accurately implement IAAF ranking formulas for point calculations | High | Draft |
| NFR-002 | The system must support multiple concurrent users viewing rankings without performance degradation | Medium | Draft |
| NFR-003 | The web application must be responsive and usable on desktop browsers | Medium | Draft |
| NFR-004 | The system must maintain data integrity ensuring athletes are consistently categorized | High | Draft |
| NFR-005 | Point calculations must complete within 5 seconds after all athlete results are recorded | Medium | Draft |
| NFR-006 | The application must use PostgreSQL for data persistence | High | Draft |
| NFR-007 | The application must be built using Spring Boot and Vaadin frameworks | High | Draft |
| NFR-008 | The system must use Flyway for database schema migrations | High | Draft |
| NFR-009 | The system must use jOOQ for type-safe database access | High | Draft |
