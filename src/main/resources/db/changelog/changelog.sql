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

--changeset vlad28x:000001-insert-some-data
insert into project(name, status, priority, start_date, completion_date, created_at, updated_at)
values ('Project Management System', 'ACTIVE', 1, '2022-01-01', '2022-09-15', (select current_timestamp), (select current_timestamp)),
       ('Car Wash Management System', 'ACTIVE', 2, '2022-02-01', '2022-06-17', (select current_timestamp), (select current_timestamp)),
       ('Task Tracker', 'NOT_STARTED', 3, '2022-02-13', '2022-07-01', (select current_timestamp), (select current_timestamp));
insert into task(name, description, status, priority, created_at, updated_at, project_id)
values ('TASK-1: Create database', 'Create database in PostgreSQL DBMS', 'DONE', 1, (select current_timestamp), (select current_timestamp), 1),
       ('TASK-2: Application Architecture', 'Create Application Architecture', 'IN_PROGRESS', 2, (select current_timestamp), (select current_timestamp), 1),
       ('TASK-3: Entities', 'Create entities', 'TO_DO', 3, (select current_timestamp), (select current_timestamp), 1),
       ('TASK-4: Migrations', 'Add migrations', 'TO_DO', 3, (select current_timestamp), (select current_timestamp), 1),
       ('TASK-1: Create database', 'Create database in ЬнЫЙД DBMS', 'IN_PROGRESS', 1, (select current_timestamp), (select current_timestamp), 2),
       ('TASK-2: Application Architecture', 'Create Application Architecture', 'IN_PROGRESS', 2, (select current_timestamp), (select current_timestamp), 2),
       ('TASK-3: Fix a bug', 'Fix a bug in the controller', 'TO_DO', 1, (select current_timestamp), (select current_timestamp), 2),
       ('TASK-1: Create database', 'Create database in PostgreSQL DBMS', 'DONE', 1, (select current_timestamp), (select current_timestamp), null),
       ('TASK-2: Application Architecture', 'Create Application Architecture', 'IN_PROGRESS', 2, (select current_timestamp), (select current_timestamp), null),
       ('TASK-3: Entities', 'Create entities', 'TO_DO', 3, (select current_timestamp), (select current_timestamp), null);