# Scala / sbt Conventions (ZIO + jsoniter)

## Style & Types
- No `null`. Use `Option`, `Either`, and ZIO for effects/errors.
- Prefer opaque types for tiny domain wrappers; expose smart constructors as needed.
- Functional-first: immutability, total functions when feasible.
- JSON: jsoniter-scala; explicit codecs in companions (avoid magic derivation on hot paths).
- One class/ADT per file. File name equals public type.

## Layout & Build
- Source: `src/main/scala`, Tests: `src/test/scala`
- Organize by feature/domain.
- Separate effectful ports/adapters from pure domain.

## Testing
- Prefer ZIO Test. 
- Avoid shared mutable state
- Generators via zio-test / scalacheck.
