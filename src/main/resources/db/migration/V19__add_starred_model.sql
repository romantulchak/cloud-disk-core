CREATE TABLE IF NOT EXISTS starred
(
    id         bigserial not null primary key,
    user_id    bigint    not null references users,
    element_id bigint    not null,
    added      timestamp not null default now()
);
