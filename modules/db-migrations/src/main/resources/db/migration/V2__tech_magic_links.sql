-- tech.magic_links: HMAC-only (no raw token, no email)
CREATE TABLE IF NOT EXISTS tech.magic_links (
  hash         text        PRIMARY KEY,               -- base64url(HMAC-SHA256(token))
  user_id      uuid        NOT NULL,
  purpose      text        NOT NULL,                  -- e.g. 'login'
  created_at   timestamptz NOT NULL DEFAULT now(),
  expires_at   timestamptz NOT NULL,
  used_at      timestamptz                            -- null = not used
);

-- Fast lookups for “still valid, not used”
CREATE INDEX IF NOT EXISTS magic_links_valid_idx
  on tech.magic_links (expires_at)
  WHERE used_at IS NULL;

-- Optional housekeeping
CREATE INDEX IF NOT EXISTS magic_links_used_idx on tech.magic_links(used_at);
