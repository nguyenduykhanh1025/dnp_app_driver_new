use eport;
create table user_devices (
	id int AUTO_INCREMENT primary key,
    user_token varchar(100) not null,
    device_token varchar(100) not null,
    user_type int not null comment '1: Logistic, 2: Driver, 3: Staff',
    create_time datetime null,
    create_by varchar(50) null,
    update_time datetime null,
    update_by varchar(50) null
);

create table notifications (
	id int auto_increment primary key,
	title varchar(255) not null,
    content text not null,
    create_time datetime null,
    create_by varchar(50) null,
    update_time datetime null,
    update_by varchar(50) null
);

create table notification_receiver (
	id int auto_increment primary key,
    notification_id int not null,
    user_device_id int not null,
    create_time datetime null,
    create_by datetime null,
    update_time datetime null,
    update_by datetime null
);