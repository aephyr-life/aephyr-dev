CREATE TABLE IF NOT EXISTS read.users_read (
  id         uuid        PRIMARY KEY,
  email_norm text        NOT NULL UNIQUE,
  status     text        NOT NULL DEFAULT 'pending',
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT chk_users_read_status CHECK (status IN ('pending','active','deactivated','blocked'))
);

CREATE OR REPLACE FUNCTION set_updated_at() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN NEW.updated_at := now(); RETURN NEW; END $$;

DROP TRIGGER IF EXISTS trg_users_read_updated_at ON users_read;
CREATE TRIGGER trg_users_read_updated_at
BEFORE UPDATE ON users_read
FOR EACH ROW EXECUTE FUNCTION set_updated_at();
