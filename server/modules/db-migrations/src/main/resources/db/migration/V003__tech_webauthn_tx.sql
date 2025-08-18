-- RUNTIME GLUE: SHORT-LIVED OPTIONS BETWEEN BEGIN()/FINISH()
CREATE TABLE IF NOT EXISTS tech.webauthn_tx (
  tx_id        UUID PRIMARY KEY DEFAULT tech.gen_random_uuid(),
  kind         TEXT NOT NULL CHECK (kind IN ('reg','auth')),
  user_id      UUID,                 -- MAY BE NULL FOR DISCOVERABLE AUTH
  request_json JSONB NOT NULL,       -- PUBLICKEYCREDENTIAL*OPTIONS AS JSON
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at   TIMESTAMPTZ NOT NULL
);

COMMENT ON TABLE tech.webauthn_tx IS
  'Ephemeral WebAuthn transactions (challenge/options) with TTL. Safe to purge.';

CREATE INDEX IF NOT EXISTS idx_webauthn_tx_expires_at
  ON tech.webauthn_tx(expires_at);
