ALTER TABLE file ADD COLUMN IF NOT EXISTS remove_type varchar not null default 'SAVED';
ALTER TABLE folder ADD COLUMN IF NOT EXISTS remove_type varchar not null default 'SAVED';
