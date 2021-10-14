ALTER TABLE file ADD COLUMN IF NOT EXISTS noticed bool not null default false;
ALTER TABLE folder ADD COLUMN IF NOT EXISTS noticed bool not null default false;
