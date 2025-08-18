-- DOMAIN: PERSONALLY IDENTIFIABLE USERS
CREATE TABLE IF NOT EXISTS pii."user" (
  id            UUID PRIMARY KEY DEFAULT tech.gen_random_uuid(),

  -- Natural identifiers (don’t authenticate with these directly!)
  email         CITEXT UNIQUE,                -- login hint, for notices
  phone         TEXT UNIQUE,                  -- optional, e.g. SMS fallback

  -- Human-readable
  display_name  TEXT,                         -- “Martin Pallmann”

  -- Lifecycle
  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  disabled_at   TIMESTAMPTZ,                  -- soft-disable / block user

  -- Audit
  last_login_at TIMESTAMPTZ,                  -- updated by auth layer
  last_login_ip INET
);

COMMENT ON TABLE pii."user" IS
  'Primary human user table (PII domain). Minimal, no auth secrets inside.';

COMMENT ON COLUMN pii."user".email IS
  'Optional natural identifier (username / contact). Used as hint, not as credential.';

CREATE INDEX IF NOT EXISTS idx_user_email ON pii."user"(email);
CREATE INDEX IF NOT EXISTS idx_user_last_login ON pii."user"(last_login_at DESC);

-- keep updated_at fresh
CREATE OR REPLACE FUNCTION pii.set_user_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_user_updated_at
BEFORE UPDATE ON pii."user"
FOR EACH ROW
EXECUTE FUNCTION pii.set_user_updated_at();
