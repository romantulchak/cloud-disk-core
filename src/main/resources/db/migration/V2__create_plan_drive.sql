CREATE TABLE IF NOT EXISTS plan
(
    id   bigserial   not null primary key,
    name varchar(90) not null
);

CREATE TABLE IF NOT EXISTS drive
(
    id        bigserial    not null primary key,
    name      varchar(100) not null unique,
    create_at timestamp    not null default now(),
    owner_id  bigint       not null references users,
    plan_id   bigint       not null references plan
);
