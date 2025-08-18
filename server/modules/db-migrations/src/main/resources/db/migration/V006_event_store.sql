-- MINIMAL DOMAIN EVENT STORE
CREATE TABLE IF NOT EXISTS event.store (
  seq            BIGSERIAL PRIMARY KEY,        -- GLOBAL ORDER
  aggregate_id   UUID NOT NULL,
  aggregate_type TEXT NOT NULL,                -- E.G. 'AuthUser'
  event_type     TEXT NOT NULL,                -- E.G. 'PasskeyRegistered'
  event_data     JSONB NOT NULL,               -- PAYLOAD
  metadata       JSONB,                        -- HEADERS, CAUSATION ID, USER, IP, ...
  occurred_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_event_store_agg
  ON event.store(aggregate_id, seq);

CREATE INDEX IF NOT EXISTS idx_event_store_types
  ON event.store(aggregate_type, event_type, occurred_at);
