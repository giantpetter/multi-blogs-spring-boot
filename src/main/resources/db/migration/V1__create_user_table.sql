create table user
(
    ID         int primary key auto_increment,
    USERNAME       varchar(100) unique,
    PASSWORD   varchar(100),
    avatar     varchar(100),
    created_at timestamp,
    updated_at timestamp
);