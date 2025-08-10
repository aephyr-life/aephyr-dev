-- DEV ONLY
SET search_path = read, public;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO read.users_read (id, email_norm, status)
SELECT gen_random_uuid(), 'dev' || i || '@example.com', 'active'
FROM generate_series(1, 25) s(i)
ON CONFLICT DO NOTHING;
