create table vehicles (
 id serial not null,
 vehicle_name varchar(255),
 owner varchar(255),
 ble_hardware_mac varchar(255),
 insert_date timestamp not null,
 last_update timestamp,
 version int4 not null default 0,
 is_deleted bit not null default 0,
 primary key (id)
 ) engine=InnoDB;