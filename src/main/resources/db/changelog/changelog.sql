--liquibase formatted sql

--changeset vlad28x:000000-create-table-project
create table project
(
    id              bigserial primary key,
    name            varchar(128) not null,
    status          varchar(128) not null,
    priority        int          not null,
    start_date      date,
    completion_date date,
    created_at      timestamp    not null,
    updated_at      timestamp    not null
);

--changeset vlad28x:000001-create-table-project
create table task
(
    id          bigserial primary key,
    name        varchar(128) not null,
    description varchar(255) not null,
    status      varchar(128) not null,
    priority    int          not null,
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    project_id  int8 references project
        on delete cascade
        on update cascade
);