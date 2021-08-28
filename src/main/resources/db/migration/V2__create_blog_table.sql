create table blog
(
    ID          bigint primary key auto_increment,
    USER_ID     bigint,
    title       varchar(30),
    description varchar(100),
    content     text,
    atIndex     bool,
    created_at  timestamp,
    updated_at  timestamp
);