-- DEV SEED: TEST USER
-- ───────────────────────────────────────────────
-- This inserts a single test user into pii.user
-- for local development & WebAuthn registration.
-- Not for production use.

INSERT INTO pii."user" (id, email, display_name, created_at, updated_at)
VALUES (
  '11111111-1111-1111-1111-111111111111',   -- fixed UUID for test user
  'test@example.com',
  'Test User',
  NOW(),
  NOW()
)
ON CONFLICT (email) DO NOTHING;
