CREATE TABLE MEMBER
(
    id             bigInt AUTO_INCREMENT PRIMARY KEY,
    email          varchar(50) UNIQUE NOT NULL,
    password       varchar(20)        NOT NULL,
    nickname       varchar(15) UNIQUE NOT NULL,
    occupationType varchar(30)        NOT NULL,
    role           varchar(10)        NOT NULL,
    created_at     DateTime           NOT NULL,
    updated_at     DateTime           NOT NULL
);

CREATE TABLE TAG
(
    id         bigInt AUTO_INCREMENT PRIMARY KEY,
    member_id  bigInt,
    name       varchar(20) NOT NULL,
    tag_type   varchar(20) NOT NULL,
    created_at dateTime    NOT NULL,
    updated_at dateTime    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES MEMBER (id),
    UNIQUE (name, tag_type)
);

create TABLE TODO
(
    id              bigInt AUTO_INCREMENT PRIMARY KEY,
    member_id       bigInt      NOT NULL,
    tag_id          bigInt      NOT NULL,
    title           varchar(20) NOT NULL,
    description     varchar(50) NOT NULL,
    complete_status varchar(20) NOT NULL,
    is_active       varchar(1)  NOT NULL,
    created_at      dateTime    NOT NULL,
    updated_at      dateTime    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES MEMBER (id),
    FOREIGN KEY (tag_id) REFERENCES TAG (id)
);