CREATE TABLE IF NOT EXISTS trash
(
    id         bigserial not null primary key,
    name       varchar   not null unique,
    drive_id   bigint    not null references drive,
    full_path  varchar   not null,
    short_path varchar   not null
);
