CREATE SCHEMA IF NOT EXISTS events;

CREATE TABLE IF NOT EXISTS events.events (
  stream_id   TEXT        NOT NULL,
  version     BIGINT      NOT NULL,                     -- 1-based within stream
  event_type  TEXT        NOT NULL,
  payload     JSONB       NOT NULL,
  metadata    JSONB       NOT NULL,
  recorded_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  PRIMARY KEY (stream_id, version)
);

CREATE INDEX IF NOT EXISTS events__by_stream
  on events.events(stream_id, version);
