create table vehicles (
    id int not null,
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
    id int not null,
    latitude varchar(255),
    longitude varchar(255),
    chatid int,
    userid varchar(255),
    timezone varchar(100),
    insert_date timestamp not null,
    last_update timestamp,
    version int4 not null default 0,
    is_deleted bit not null default 0,
    vehicleid int not null,
    primary key (id)
);

create table favorite_selections (
    id int not null,
    is_favorite bit not null,
    userid varchar(255),
    vehicleid int not null,
    insert_date timestamp not null,
    last_update timestamp,
    version int4 not null default 0,
    is_deleted bit not null default 0,
    primary key (id)
);
alter table favorite_selections
    add foreign key (vehicleid)
    references vehicles (id);

alter table positions
    add foreign key (vehicleid)
    references vehicles (id);