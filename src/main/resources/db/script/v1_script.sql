use exe201;
create table dance_type
(
    id          int auto_increment
        primary key,
    type        varchar(100) null,
    description varchar(255) null,
    created_at  datetime     null,
    updated_at  datetime     null
);

create table flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index flyway_schema_history_s_idx
    on flyway_schema_history (success);

create table otp
(
    id         int auto_increment
        primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    email      varchar(255) null,
    expires_at datetime(6)  null,
    otp_code   varchar(255) null
);

create table permission
(
    id         int auto_increment
        primary key,
    name       varchar(255) null,
    created_at datetime(6)  null,
    updated_at datetime(6)  null
);

create table role
(
    id         int auto_increment
        primary key,
    name       varchar(100) null,
    created_at datetime(6)  null,
    updated_at datetime(6)  null
);

create table roles_permissions
(
    id            int auto_increment
        primary key,
    role_id       int not null,
    permission_id int not null,
    constraint roles_permissions_ibfk_1
        foreign key (role_id) references role (id),
    constraint roles_permissions_ibfk_2
        foreign key (permission_id) references permission (id)
);

create index permission_id
    on roles_permissions (permission_id);

create index role_id
    on roles_permissions (role_id);

create table status
(
    id          int auto_increment
        primary key,
    status_name varchar(100)                           null,
    status_type enum ('ACTIVE', 'INACTIVE', 'PENDING') null,
    description varchar(255)                           null,
    created_at  datetime                               null,
    updated_at  datetime                               null
);

create table area
(
    id         int auto_increment
        primary key,
    district   varchar(100) null,
    ward       varchar(100) null,
    city       varchar(100) null,
    created_at datetime     null,
    updated_at datetime     null,
    status_id  int          not null,
    constraint area_ibfk_1
        foreign key (status_id) references status (id)
);

create index status_id
    on area (status_id);

create table article
(
    id          int auto_increment
        primary key,
    title       varchar(255) null,
    content     varchar(255) null,
    author_name varchar(255) null,
    thumbnail   tinytext     null,
    created_at  datetime     null,
    updated_at  datetime     null,
    status_id   int          not null,
    constraint article_ibfk_1
        foreign key (status_id) references status (id)
);

create index status_id
    on article (status_id);

create table subscription
(
    id         int auto_increment
        primary key,
    name       varchar(100)   null,
    duration   int            null,
    content    varchar(255)   null,
    price      decimal(10, 2) null,
    created_at datetime       null,
    updated_at datetime       null,
    status_id  int            not null,
    constraint subscription_ibfk_1
        foreign key (status_id) references status (id)
);

create index status_id
    on subscription (status_id);

create table users
(
    id         int auto_increment
        primary key,
    avatar     varchar(255) null,
    age        int          null,
    name       varchar(255) null,
    email      varchar(255) null,
    phone      varchar(255) null,
    password   varchar(255) null,
    created_at datetime     null,
    updated_at datetime     null,
    area_id    int          null,
    status_id  int          not null,
    constraint users_ibfk_1
        foreign key (area_id) references area (id),
    constraint users_ibfk_2
        foreign key (status_id) references status (id)
);

create table choreography
(
    id              int auto_increment
        primary key,
    user_id         int            not null,
    subscription_id int            null,
    about           varchar(255)   null,
    year_experience int            null,
    price           decimal(10, 2) null,
    status_id       int            not null,
    created_at      datetime(6)    null,
    updated_at      datetime(6)    null,
    constraint choreography_ibfk_2
        foreign key (user_id) references users (id),
    constraint choreography_ibfk_3
        foreign key (subscription_id) references subscription (id),
    constraint choreography_ibfk_4
        foreign key (status_id) references status (id)
);


create index status_id
    on choreography (status_id);

create index subscription_id
    on choreography (subscription_id);

create index user_id
    on choreography (user_id);

create table choreography_dance_type
(
    choreography_id int not null,
    dance_type_id   int not null,
    primary key (choreography_id, dance_type_id),
    constraint choreography_dance_type_ibfk_1
        foreign key (choreography_id) references choreography (id)
            on delete cascade,
    constraint choreography_dance_type_ibfk_2
        foreign key (dance_type_id) references dance_type (id)
            on delete cascade
);

create index dance_type_id
    on choreography_dance_type (dance_type_id);

create table dancer
(
    id               int auto_increment
        primary key,
    dancer_nick_name varchar(255)   null,
    user_id          int            not null,
    about            varchar(255)   null,
    year_experience  int            null,
    team_size        int            null,
    price            decimal(10, 2) null,
    subscription_id  int            null,
    status_id        int            not null,
    created_at       datetime(6)    null,
    updated_at       datetime(6)    null,
    constraint dancer_ibfk_2
        foreign key (user_id) references users (id),
    constraint dancer_ibfk_3
        foreign key (subscription_id) references subscription (id),
    constraint dancer_ibfk_4
        foreign key (status_id) references status (id)
);


create index status_id
    on dancer (status_id);

create index subscription_id
    on dancer (subscription_id);

create index user_id
    on dancer (user_id);

create table dancer_dance_type
(
    dancer_id     int not null,
    dance_type_id int not null,
    primary key (dancer_id, dance_type_id),
    constraint dancer_dance_type_ibfk_1
        foreign key (dancer_id) references dancer (id)
            on delete cascade,
    constraint dancer_dance_type_ibfk_2
        foreign key (dance_type_id) references dance_type (id)
            on delete cascade
);

create index dance_type_id
    on dancer_dance_type (dance_type_id);

create table performance
(
    id          int auto_increment
        primary key,
    user_id     int          not null,
    media_url   varchar(255) null,
    media_type  varchar(255) null,
    purpose     varchar(255) null,
    description varchar(255) null,
    created_at  datetime     null,
    updated_at  datetime     null,
    status_id   int          not null,
    constraint performance_ibfk_1
        foreign key (user_id) references users (id),
    constraint performance_ibfk_2
        foreign key (status_id) references status (id)
);

create table booking
(
    id                          int auto_increment
        primary key,
    customer_id                 int          not null,
    dancer_id                   int          not null,
    choreography_id             int          not null,
    dance_type_id               int          not null,
    area_id                     int          not null,
    status_id                   int          not null,
    booking_date                datetime     null,
    start_time                  datetime     null,
    end_time                    datetime     null,
    address                     varchar(255) null,
    detail                      varchar(255) null,
    update_booking_date         datetime     null,
    booking_status              varchar(255) null,
    customer_phone              varchar(255) null,
    performance_id              int          not null,
    created_at                  datetime(6)  null,
    updated_at                  datetime(6)  null,
    number_of_training_sessions int          null,
    dancer_phone                varchar(255) null,
    choreography_phone          varchar(255) null,
    constraint booking_ibfk_1
        foreign key (customer_id) references users (id),
    constraint booking_ibfk_2
        foreign key (dancer_id) references dancer (id),
    constraint booking_ibfk_3
        foreign key (choreography_id) references choreography (id),
    constraint booking_ibfk_4
        foreign key (dance_type_id) references dance_type (id),
    constraint booking_ibfk_5
        foreign key (area_id) references area (id),
    constraint booking_ibfk_6
        foreign key (status_id) references status (id),
    constraint booking_ibfk_7
        foreign key (performance_id) references performance (id)
);

create index area_id
    on booking (area_id);

create index choreography_id
    on booking (choreography_id);

create index customer_id
    on booking (customer_id);

create index dance_type_id
    on booking (dance_type_id);

create index dancer_id
    on booking (dancer_id);

create index performance_id
    on booking (performance_id);

create index status_id
    on booking (status_id);

create table booking_feedback
(
    id           int auto_increment
        primary key,
    from_user_id int          not null,
    to_user_id   int          not null,
    rating       int          null,
    comment      varchar(255) null,
    created_at   datetime     null,
    updated_at   datetime     null,
    status_id    int          not null,
    booking_id   int          not null,
    constraint booking_feedback_ibfk_1
        foreign key (from_user_id) references users (id),
    constraint booking_feedback_ibfk_2
        foreign key (to_user_id) references users (id),
    constraint booking_feedback_ibfk_3
        foreign key (status_id) references status (id),
    constraint booking_feedback_ibfk_4
        foreign key (booking_id) references booking (id)
);

create index booking_id
    on booking_feedback (booking_id);

create index from_user_id
    on booking_feedback (from_user_id);

create index status_id
    on booking_feedback (status_id);

create index to_user_id
    on booking_feedback (to_user_id);

create table chat
(
    id         int auto_increment
        primary key,
    booking_id int      not null,
    created_at datetime null,
    updated_at datetime null,
    status_id  int      not null,
    constraint chat_ibfk_1
        foreign key (booking_id) references booking (id),
    constraint chat_ibfk_2
        foreign key (status_id) references status (id)
);

create index booking_id
    on chat (booking_id);

create index status_id
    on chat (status_id);

create index status_id
    on performance (status_id);

create index user_id
    on performance (user_id);

create table user_subscription
(
    id              int auto_increment
        primary key,
    user_id         int         not null,
    subscription_id int         not null,
    from_date       datetime    null,
    to_date         datetime    null,
    status_id       int         not null,
    created_at      datetime(6) null,
    updated_at      datetime(6) null,
    constraint user_subscription_ibfk_1
        foreign key (user_id) references users (id),
    constraint user_subscription_ibfk_2
        foreign key (subscription_id) references subscription (id),
    constraint user_subscription_ibfk_3
        foreign key (status_id) references status (id)
);

create index status_id
    on user_subscription (status_id);

create index subscription_id
    on user_subscription (subscription_id);

create index user_id
    on user_subscription (user_id);

create index area_id
    on users (area_id);

create index status_id
    on users (status_id);

create table users_roles
(
    id      int auto_increment
        primary key,
    user_id int not null,
    role_id int not null,
    constraint users_roles_ibfk_1
        foreign key (user_id) references users (id),
    constraint users_roles_ibfk_2
        foreign key (role_id) references role (id)
);

create index role_id
    on users_roles (role_id);

create index user_id
    on users_roles (user_id);


