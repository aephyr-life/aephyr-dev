-- READ MODEL FOR ADMIN/UI LISTS
CREATE OR REPLACE VIEW read.user_credentials AS
SELECT
  c.user_id,
  ENCODE(c.credential_id, 'base64') AS credential_id_b64,
  c.aaguid,
  c.transports,
  c.backup_eligible,
  c.backed_up,
  c.sign_count,
  c.created_at,
  c.last_used_at
FROM auth.user_credential c;
