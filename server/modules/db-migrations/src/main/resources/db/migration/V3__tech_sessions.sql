CREATE TABLE IF NOT EXISTS tech.sessions (
  id         UUID PRIMARY KEY,
  user_id    UUID NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at TIMESTAMPTZ NOT NULL,
  last_seen  TIMESTAMPTZ NOT NULL DEFAULT now(),
  revoked_at TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS tech_sessions_user ON tech.sessions(user_id);
