# Swift / Xcode Conventions

## Style
- Swift Concurrency (`async/await`) over callbacks where possible.
- Avoid force-unwraps; prefer `guard` / `if let`.
- Codable for models; explicit date/number formatting.
- Prefer Foundation.Measurement for mass, energy etc.

## Layout & Build
- Workspace recommended. Dedicated test target per app/framework.
- Keep DerivedData out of VCS; deterministic paths in CI.

## Testing
- XCTest; no live network in unit tests—use fakes.
- One type per file; name matches public type.
