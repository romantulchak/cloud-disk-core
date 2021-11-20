CREATE TABLE IF NOT EXISTS rename_history
(
    id         bigserial    not null primary key,
    element_id bigint       not null,
    type       varchar      not null,
    user_id    bigint       not null references users,
    date       timestamp    not null default now(),
    name       varchar(195) not null,
    old_name   varchar(195) not null
);