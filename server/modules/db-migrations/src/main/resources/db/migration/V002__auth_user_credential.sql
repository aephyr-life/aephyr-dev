-- DOMAIN: PERSISTENT WEBAUTHN CREDENTIALS
CREATE TABLE IF NOT EXISTS auth.user_credential (
  id                UUID PRIMARY KEY DEFAULT tech.gen_random_uuid(),
  user_id           UUID NOT NULL
    REFERENCES pii."user"(id) ON DELETE CASCADE,
  credential_id     BYTEA NOT NULL UNIQUE,      -- RAW CREDENTIAL ID (BINARY)
  public_key_cose   BYTEA NOT NULL,             -- COSE-ENCODED PUBLIC KEY
  sign_count        BIGINT NOT NULL DEFAULT 0,
  aaguid            UUID,
  transports        TEXT[],                     -- 'usb'|'nfc'|'ble'|'internal'|'hybrid',...
  backup_eligible   BOOLEAN,
  backed_up         BOOLEAN,
  uv_requirement    TEXT,                       -- 'required'|'preferred'|'discouraged'
  created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  last_used_at      TIMESTAMPTZ
);

COMMENT ON TABLE  auth.user_credential IS
  'Persistent WebAuthn passkeys (domain data, not ephemeral).';
COMMENT ON COLUMN auth.user_credential.credential_id IS
  'Raw credentialId (binary). Encode/decode Base64URL at API boundary.';

CREATE INDEX IF NOT EXISTS idx_user_credential_user_id
  ON auth.user_credential(user_id);

CREATE INDEX IF NOT EXISTS idx_user_credential_last_used
  ON auth.user_credential(last_used_at DESC);

-- OPTIONAL UX HINT: USER PREFERRED CREDENTIAL
CREATE TABLE IF NOT EXISTS auth.user_pref (
  user_id        UUID PRIMARY KEY
    REFERENCES pii."user"(id) ON DELETE CASCADE,
  preferred_cred BYTEA
);

COMMENT ON TABLE auth.user_pref IS 'Optional UX hint for a user''s preferred credential.';
