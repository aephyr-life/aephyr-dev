# Kotlin / Gradle Conventions

## Style & Types
- Prefer `@JvmInline value class` for tiny domain wrappers (IDs, units, amounts).
- Avoid `!!`. Use null-safety operators and sealed classes/ADTs for sum types.
- Error handling via `Result`/Either-like patterns, not exceptions for control flow.
- One class per file. File name equals public class name.

## Layout & Build
- Source: `src/main/kotlin`, Tests: `src/test/kotlin`
- Versions pinned in `gradle/libs.versions.toml`
- Use Gradle wrapper (`./gradlew`), caching, and parallel builds.

## Testing
- JUnit 5 (+Kotest optional). No network in unit tests; use fakes/stubs.
- Every bugfix adds a regression test.
