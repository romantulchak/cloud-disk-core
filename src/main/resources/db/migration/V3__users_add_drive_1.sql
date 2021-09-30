ALTER TABLE users ADD COLUMN IF NOT EXISTS drive_id bigint references drive
