-- one row per aggregate stream (current version)
CREATE TABLE IF NOT EXISTS events.streams (
  aggregate_type  text   NOT NULL,         -- e.g. 'User'
  aggregate_id    uuid   NOT NULL,
  version         bigint NOT NULL,         -- last committed version
  PRIMARY KEY (aggregate_type, aggregate_id)
);

-- append-only event log (global order + per-aggregate version)
CREATE TABLE IF NOT EXISTS events.events (
  seq             bigserial PRIMARY KEY,   -- global monotonic id
  aggregate_type  text      NOT NULL,
  aggregate_id    uuid      NOT NULL,
  version         bigint    NOT NULL,      -- 1..N within a stream
  event_type      text      NOT NULL,      -- e.g. 'UserRegistered'
  payload         jsonb     NOT NULL,      -- event data (no secrets ideally)
  metadata        jsonb     NOT NULL,      -- causation/correlation/user/ip/etc
  occurred_at     timestamptz NOT NULL DEFAULT now(),
  UNIQUE (aggregate_type, aggregate_id, version)
);

-- helpful index for projectors by global ordering
CREATE INDEX IF NOT EXISTS events__by_seq
  ON events.events(seq);

-- helpful index for per-aggregate loads
CREATE INDEX IF NOT EXISTS events__by_stream
  ON events.events(aggregate_type, aggregate_id, version);

-- rights
-- aephyr_app: INSERT/SELECT on events.events, SELECT/INSERT/UPDATE on events.streams
-- projector: SELECT on events.events
