create table vehicles (
    id mediumint not null auto_increment,
    vehicle_name varchar(255),
    owner varchar(255),
    ble_hardware_mac varchar(255),
    insert_date timestamp not null,
    last_update timestamp,
    version int4 not null default 0,
    is_deleted bit not null default 0,
    primary key (id)
);

create table positions (
    id mediumint not null auto_increment,
    latitude varchar(255),
    longitude varchar(255),
    chatid int,
    userid varchar(255),
    timezone varchar(100),
    user_timestamp varchar(255),
    insert_date timestamp not null,
    last_update timestamp,
    version int4 not null default 0,
    is_deleted bit not null default 0,
    vehicle_id mediumint not null,
    primary key (id)
);

create table vehicle_associations (
    id mediumint not null auto_increment,
    is_favorite bit not null,
    userid varchar(255),
    vehicle_id mediumint not null,
    insert_date timestamp not null,
    last_update timestamp,
    version int4 not null default 0,
    is_deleted bit not null default 0,
    primary key (id)
);


alter table vehicle_associations
    add foreign key (vehicle_id)
    references vehicles (id);

alter table positions
    add foreign key (vehicle_id)
    references vehicles (id);

