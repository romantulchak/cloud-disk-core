CREATE TABLE IF NOT EXISTS pre_remove
(
    id             bigserial not null primary key,
    remove_date    timestamp not null,
    added_to_trash timestamp not null,
    element_id     bigint    not null,
    path           varchar   not null
);

