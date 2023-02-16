create table Client
(
    id          varchar(50) not null primary key,
    name        varchar(50)
);

create table address
(
    client      varchar(50) not null references Client (id),
    street      varchar(50)
);

create table phone
(
    id          bigserial not null primary key,
    number      varchar(50),
    client_id   varchar(50) not null references Client (id)
);
create index idx_client_client_id on phone (client_id);
