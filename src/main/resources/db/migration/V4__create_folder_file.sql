CREATE TABLE IF NOT EXISTS folder
(
    id              bigserial   not null primary key,
    name            varchar(90) not null unique,
    drive_id        bigint references drive,
    link            uuid        not null,
    create_at       timestamp   not null default now(),
    upload_at       timestamp   not null default now(),
    owner_id        bigint      not null references users,
    has_link_access boolean              default false
);

CREATE TABLE IF NOT EXISTS file
(
    id              bigserial    not null primary key,
    name            varchar(195) not null unique,
    create_at       timestamp    not null default now(),
    upload_at       timestamp    not null default now(),
    size            bigint       not null default 0,
    folder_id       bigint       null references folder,
    drive_id        bigint       null references drive,
    link            uuid         not null,
    has_link_access boolean               default false
);
