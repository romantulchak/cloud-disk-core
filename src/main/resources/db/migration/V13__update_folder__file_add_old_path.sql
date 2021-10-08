ALTER TABLE file ADD COLUMN IF NOT EXISTS old_path varchar null;
ALTER TABLE folder ADD COLUMN IF NOT EXISTS old_path varchar null;
