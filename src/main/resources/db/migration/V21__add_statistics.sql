CREATE TABLE IF NOT EXISTS history
(
    id         bigserial not null primary key,
    element_id bigint    not null,
    user_id    bigint    not null references users,
    type       varchar   not null,
    date       timestamp not null default now()

)