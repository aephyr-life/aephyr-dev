-- ─────────────────────────────────────────────────────────────────────────────
-- ## Users
-- ### aephyr_migrator
-- Flyway migrations, is owner of all schemas.
--
-- ### aephyr_app:       for the general app
-- General app, has only read rights on schema >read<.
--
-- ### aephyr_projector: for the general projector
-- Projector, can read >events<, >pii<, >tech<, writes to >read<
--
-- ─────────────────────────────────────────────────────────────────────────────

GRANT CREATE, USAGE ON SCHEMA public TO aephyr_migrator;

CREATE SCHEMA IF NOT EXISTS events;
CREATE SCHEMA IF NOT EXISTS pii;
CREATE SCHEMA IF NOT EXISTS read;
CREATE SCHEMA IF NOT EXISTS tech;

ALTER SCHEMA events OWNER TO aephyr_migrator;
ALTER SCHEMA pii    OWNER TO aephyr_migrator;
ALTER SCHEMA read   OWNER TO aephyr_migrator;
ALTER SCHEMA tech   OWNER TO aephyr_migrator;

-- Harden. Please use always schemas. Don't use the public schema
ALTER ROLE aephyr_app       SET search_path = '';
ALTER ROLE aephyr_projector SET search_path = '';
REVOKE ALL ON SCHEMA public FROM public;

-- pii: app can USAGE + SELECT; grant narrow writes per-table
GRANT USAGE ON SCHEMA pii TO aephyr_app;
GRANT SELECT ON ALL TABLES IN SCHEMA pii TO aephyr_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA pii
  GRANT SELECT ON TABLES TO aephyr_app;

-- App can access the schema itself
GRANT USAGE ON SCHEMA read TO aephyr_app;

-- App can read existing tables
GRANT SELECT ON ALL TABLES IN SCHEMA read TO aephyr_app;

-- Future tables created by the migrator are readable by the app
ALTER DEFAULT PRIVILEGES FOR USER aephyr_migrator IN SCHEMA read
  GRANT SELECT ON TABLES TO aephyr_app;

-- (If any sequences exist in read)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA read TO aephyr_app;
ALTER DEFAULT PRIVILEGES FOR USER aephyr_migrator IN SCHEMA read
  GRANT USAGE, SELECT ON SEQUENCES TO aephyr_app;

-- Example: allow app to write only what you need
--GRANT INSERT(id, email_norm, created_at), UPDATE(email_norm), DELETE
--  ON pii.user_email
--  TO aephyr_app;

GRANT USAGE ON SCHEMA read TO aephyr_projector;
GRANT INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA read TO aephyr_projector;
ALTER DEFAULT PRIVILEGES FOR USER aephyr_migrator IN SCHEMA read
  GRANT INSERT, UPDATE, DELETE ON TABLES TO aephyr_projector;

-- projector: read-only on pii
GRANT USAGE ON SCHEMA pii TO aephyr_projector;
GRANT SELECT ON ALL TABLES IN SCHEMA pii TO aephyr_projector;

-- tech: app owns runtime writes
GRANT USAGE ON SCHEMA tech TO aephyr_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tech TO aephyr_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA tech
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO aephyr_app;

-- projector on tech: read-only, unless updating outbox.claimed_at/published_at
GRANT USAGE ON SCHEMA tech TO aephyr_projector;
GRANT SELECT ON ALL TABLES IN SCHEMA tech TO aephyr_projector;

-- If projector needs to mark outbox rows as processed:
--GRANT UPDATE (claimed_at, published_at)
--  ON tech.outbox
--  TO aephyr_projector;


-- create trigger to prohibit mutability
--CREATE OR REPLACE FUNCTION tech.forbid_mutation_except_status()
--RETURNS trigger AS $$
--BEGIN
--  IF TG_OP = 'UPDATE' THEN
--    IF NEW.* IS DISTINCT FROM OLD.* EXCEPT (published_at, claimed_at) THEN
--      RAISE EXCEPTION 'Only status columns may be updated';
--    END IF;
--    RETURN NEW;
--  ELSIF TG_OP = 'DELETE' THEN
--    RAISE EXCEPTION 'Outbox rows are not deletable';
--  END IF;
--  RETURN NEW;
--END
--$$ LANGUAGE plpgsql;
--
--CREATE TRIGGER tech_outbox_guard
--BEFORE UPDATE OR DELETE ON tech.outbox
--FOR EACH ROW EXECUTE FUNCTION tech.forbid_mutation_except_status();
