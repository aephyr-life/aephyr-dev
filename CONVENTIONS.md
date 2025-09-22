# Monorepo Conventions

## Git & Branching
- Default branch: `main`
- Feature branches: `feature/<short-desc>`
- Squash merges via PRs
- Commit style: Conventional Commits (imperative, ≤72 chars subject)

## Structure
- Each project contains its own `CONVENTIONS.md` with language/tooling specifics.
- Shared libs under `/libs/*`.
- One class/ADT per file, even if small.
- Tests colocated per ecosystem:
  - Gradle: `src/test`
  - sbt: `src/test/scala`
  - Xcode: dedicated test target(s)

## Test Editing Policy
- Tests must be considered a contract — do not change or remove tests just to make the build pass.
- If a test is wrong or obsolete, open a discussion or PR explaining why it should be updated.
- When fixing a bug, first add or update a failing regression test, then fix production code.

## Quality Gates
- CI: lint + unit tests for changed projects must pass.
- Coverage guideline: ≥80% for changed lines.
- No `TODO` left behind without an issue.

## Tooling
- Use formatter/linters in CI (ktlint/ktfmt, scalafmt, swiftformat if applicable).
- Use `just` tasks defined in the repo for reproducible workflows.
