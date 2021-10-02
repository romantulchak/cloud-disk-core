ALTER TABLE drive ADD COLUMN IF NOT EXISTS full_path varchar;
ALTER TABLE drive ADD COLUMN IF NOT EXISTS short_path varchar;


ALTER TABLE folder ADD COLUMN IF NOT EXISTS full_path varchar;
ALTER TABLE folder ADD COLUMN IF NOT EXISTS short_path varchar;

ALTER TABLE file ADD COLUMN IF NOT EXISTS full_path varchar;
ALTER TABLE file ADD COLUMN IF NOT EXISTS short_path varchar;

