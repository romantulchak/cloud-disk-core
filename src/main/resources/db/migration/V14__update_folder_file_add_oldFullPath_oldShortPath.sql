ALTER TABLE file ADD COLUMN IF NOT EXISTS old_full_path varchar null;
ALTER TABLE file ADD COLUMN IF NOT EXISTS old_short_path varchar null;

ALTER TABLE folder ADD COLUMN IF NOT EXISTS old_full_path varchar null;
ALTER TABLE folder ADD COLUMN IF NOT EXISTS old_short_path varchar null;
