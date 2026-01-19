# AGENTS.md

This document provides instructions for AI coding agents working in this repository.

## Project Context

This is `playground-opencode`, part of the `alexandria-playground` mono-repository. The repository contains learning experiments, algorithm implementations, design patterns, and proof-of-concepts primarily using **Kotlin** and **Java** with **Gradle** builds.

## Build Commands

### All Tests
```bash
./gradlew test
```

### Single Test Class
```bash
./gradlew test --tests "com.currenjin.YourTestClassName"
```

### Single Test Method
```bash
./gradlew test --tests "com.currenjin.YourTestClassName.test_method_name"
```

### Build Without Tests
```bash
./gradlew build -x test
```

### Clean Build
```bash
./gradlew clean build
```

## Project Structure

```
src/
  main/
    kotlin/com/currenjin/     # Production code (Kotlin projects)
    java/com/currenjin/       # Production code (Java projects)
    resources/
  test/
    kotlin/com/currenjin/     # Test code (Kotlin projects)
    java/com/currenjin/       # Test code (Java projects)
```

## Code Style Guidelines

### Language Selection
- **Kotlin** is preferred for new projects
- **Java 17+** if Spring Boot 2.x is used, **Java 21** for Spring Boot 3.x or pure Kotlin
- All code, comments, and identifiers must be in **English**
- Documentation and explanations may be in **Korean** if context requires

### Naming Conventions

#### Classes & Interfaces
- PascalCase: `MyHashMap`, `GameService`, `GameError`
- Domain classes: descriptive nouns (`Player`, `Game`, `Nums`)
- Service classes: `{Domain}Service`
- Repository interfaces: `{Domain}Repository`
- Errors: `{Domain}Error` (sealed classes preferred in Kotlin)

#### Functions & Methods
- camelCase: `twoSum()`, `createGame()`, `joinGame()`
- Test methods: snake_case with descriptive behavior
  - `new_map_has_zero_size()`
  - `put_once_makes_size_1()`
  - `handles_collision_by_chaining()`

#### Variables
- camelCase: `loadFactor`, `maxPlayers`, `playerId`
- Private backing fields: prefix with underscore `_size`
- Constants: UPPER_SNAKE_CASE (rare in this codebase)

### Formatting
- Indentation: **4 spaces** (IDE default for Kotlin/Java)
- Max line length: ~120 characters (soft limit)
- Blank lines between logical sections
- Opening braces on same line

### Type Safety
- **NEVER** use `as Any`, `@ts-ignore`, `@Suppress("UNCHECKED_CAST")` without justification
- Prefer explicit types over type inference for public APIs
- Use nullable types (`?`) explicitly; avoid `!!` operator

### Error Handling
- Use **sealed classes** for domain errors (Kotlin)
```kotlin
sealed class GameError(message: String) : RuntimeException(message) {
    class RoomIsFull : GameError("Room is full")
    class InvalidTarget(message: String) : GameError(message)
}
```
- Throw specific exceptions, not generic `Exception`
- **Never** use empty catch blocks

### Test Guidelines

#### Structure (Arrange-Act-Assert)
```kotlin
@Test
fun put_then_get_returns_value() {
    // Arrange
    val map = MyHashMap<String, Int?>()
    
    // Act
    map.put("a", 1)
    
    // Assert
    assertEquals(1, map.get("a"))
}
```

#### Test Naming
- Describe behavior, not implementation
- Use snake_case for readability
- Pattern: `{condition}_{expected_result}` or `{action}_{outcome}`

#### Assertions
- Prefer JUnit 5 assertions: `assertEquals`, `assertTrue`, `assertNull`
- AssertJ available: `assertThat(actual).isEqualTo(expected)`

### TDD Workflow (When Applicable)

Follow the Red-Green-Refactor cycle:
1. **Red**: Write a failing test first
2. **Green**: Implement minimum code to pass
3. **Refactor**: Clean up while tests stay green

See `plan.md` in project root if TDD workflow is active.

## Architecture Patterns (Spring Projects)

```
domain/           # Core business logic, entities, errors
service/          # Application services, orchestration  
infrastructure/   # Repositories, external integrations
```

- **Repository**: interface with `findById()`, `save()` methods
- **Service**: `@Service` class with constructor-injected dependencies
- **Errors**: sealed classes extending `RuntimeException`

## Commit Guidelines

- Only commit when **all tests pass**
- Small, focused commits over large batches
- Separate **structural** changes (refactoring) from **behavioral** changes (features)
- Commit messages: clear, imperative mood ("Add", "Fix", "Refactor")

## Do NOT

- Suppress type errors without explicit justification
- Use empty catch blocks
- Mix refactoring with feature changes in same commit
- Introduce new dependencies without discussion
- Change unrelated files when fixing bugs
- Delete or skip failing tests to "pass"
