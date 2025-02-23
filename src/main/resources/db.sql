create table users
(
    id           serial primary key,
    full_name    varchar(50) not null,
    email        varchar(50),
    phone_number varchar(20) not null,
    home_address varchar(50),
    city         varchar(50),
    state        varchar(50),
    zip_code     varchar(20),
    country      varchar(20),
    created_at   timestamp default CURRENT_TIMESTAMP,
    updated_at   timestamp default CURRENT_TIMESTAMP
);

create table images
(
    id         serial primary key,
    url        text    not null,
    item_id    integer not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    updated_at timestamp default CURRENT_TIMESTAMP,
    constraint fk_item_id foreign key (item_id) references items (id) on delete cascade
);

create table items
(
    id              serial primary key,
    title           varchar(100) not null,
    description     text,
    location        varchar(100),
    location_coords geometry(Point, 4326),
    category        varchar(20)  not null,
    created_by      integer      not null,
    status          varchar(10) check ( status in ('LOST', 'FOUND') ),
    lost_date       date,
    found_date      date,
    created_at      timestamp default CURRENT_TIMESTAMP,
    updated_at      timestamp default CURRENT_TIMESTAMP,
    constraint fk_created_by foreign key (created_by) references users (id) on delete cascade
);

alter table items add column     status          varchar(10) check ( status in ('LOST', 'FOUND', 'RETURNED') );