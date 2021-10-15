CREATE TABLE IF NOT EXISTS element_access
(
    id          bigserial not null primary key,
    element_id  bigint    not null,
    create_at   timestamp not null default now(),
    access_type varchar   not null default 'READER'
);

ALTER TABLE file ADD COLUMN IF NOT EXISTS element_access_id bigint references element_access;
ALTER TABLE folder ADD COLUMN IF NOT EXISTS element_access_id bigint references element_access;
