ALTER TABLE file ADD COLUMN IF NOT EXISTS preview_full_path varchar;
ALTER TABLE file ADD COLUMN IF NOT EXISTS preview_short_path varchar;
ALTER TABLE file ADD COLUMN IF NOT EXISTS preview_old_full_path varchar;
ALTER TABLE file ADD COLUMN IF NOT EXISTS preview_old_short_path varchar;
ALTER TABLE file DROP COLUMN IF EXISTS preview_path;