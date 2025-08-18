-- IMMUTABLE SECURITY/AUTH AUDIT LOG
CREATE TABLE IF NOT EXISTS audit.auth_event (
  id            BIGSERIAL PRIMARY KEY,
  at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  user_id       UUID,
  credential_id BYTEA,
  event_type    TEXT NOT NULL,   -- 'REG_OK'|'REG_FAIL'|'ASSERTION_OK'|'ASSERTION_FAIL'|...
  reason        TEXT,
  ip            INET,
  ua            TEXT
);

COMMENT ON TABLE audit.auth_event IS
  'Append-only auth events for incident response & analytics.';

CREATE INDEX IF NOT EXISTS idx_auth_event_user_at
  ON audit.auth_event(user_id, at DESC);

CREATE INDEX IF NOT EXISTS idx_auth_event_type_at
  ON audit.auth_event(event_type, at DESC);
