CREATE TABLE IF NOT EXISTS upload_history
(
    id                         bigserial    not null primary key,
    element_id                 bigint       not null,
    type                       varchar      not null,
    user_id                    bigint       not null references users,
    date                       timestamp    not null default now(),
    uploaded_element_name      varchar(195) not null,
    uploaded_element_link      uuid         not null,
    uploaded_element_full_path varchar      not null,
    context                    varchar      not null
)