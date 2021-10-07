ALTER TABLE file ADD COLUMN IF NOT EXISTS trash_id bigint null references trash;
ALTER TABLE folder ADD COLUMN IF NOT EXISTS trash_id bigint null references trash;
ALTER TABLE drive ADD COLUMN IF NOT EXISTS trash_id bigint null references trash;
