-- drop table if exists products;
-- drop table if exists blocked_products;

create table categories (
    id bigint auto_increment,
    name varchar(255) not null unique,
    primary key (id)
);

create table if not exists products (
    id bigint generated always as identity,
    name varchar(255) not null,
    price decimal(10,2) not null,
    stock int not null,
    category_id bigint not null,
    description varchar(2048),
    image_file varchar(255),
    primary key (id),
    foreign key (category_id) references categories (id) on delete set null
);

create table blocked_products (
    order_id uuid,
    product_id bigint not null,
    quantity int not null,
    primary key (order_id)
);

