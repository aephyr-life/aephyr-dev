-- PER-PROJECTOR CHECKPOINTS (OFFSET/POSITION)
CREATE TABLE IF NOT EXISTS tech.projector_checkpoint (
  name       TEXT PRIMARY KEY,
  position   TEXT NOT NULL,                -- LAST SEQ OR EXTERNAL CURSOR
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE tech.projector_checkpoint IS
  'Offsets for idempotent projectors consuming the event store.';
