CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

CREATE TABLE IF NOT EXISTS users
(
    id         bigserial   not null primary key,
    first_name varchar(50) not null,
    last_name  varchar(75) not null,
    username   varchar(90) not null unique,
    email      varchar(90) not null unique,
    password   varchar     not null
);

CREATE TABLE IF NOT EXISTS role
(
    id   bigserial   not null primary key,
    name varchar(90) not null
);

CREATE TABLE IF NOT EXISTS users_role
(
    user_id bigint not null references users,
    role_id bigint not null references role
)
