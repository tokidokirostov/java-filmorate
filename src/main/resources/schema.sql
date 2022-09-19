create table IF NOT EXISTS RATING
(
    RATING_ID INTEGER not null,
    RATING    CHARACTER VARYING(20),
    constraint RATING_PK
        primary key (RATING_ID)
);
create table IF NOT EXISTS GANRES
(
    GANRE_ID INTEGER not null,
    GANRE    CHARACTER VARYING(20),
    constraint GANRES_PK
        primary key (GANRE_ID)
);
create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING     not null,
    LOGIN    CHARACTER VARYING(50) not null,
    NAME     CHARACTER VARYING(50),
    BIRTHDAY TIMESTAMP             not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDS
(
    FRIENDS_ID     INTEGER auto_increment,
    USER_ID_PK     INTEGER,
    USER_FRIEND_ID INTEGER,
    EXECPT         BOOLEAN,
    constraint FRIENDS_PK
        primary key (FRIENDS_ID),
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID_PK) references USERS,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (USER_FRIEND_ID) references USERS
);
create table IF NOT EXISTS FILM
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(50) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE TIMESTAMP,
    DURATION     INTEGER,
    RATING_ID_PK INTEGER,
    constraint FILM_PK
        primary key (FILM_ID),
    constraint FILM_RATING_RATING_ID_FK
        foreign key (RATING_ID_PK) references RATING
);

create table IF NOT EXISTS FILM_GANRES
(
    FILM_GANRES_ID INTEGER auto_increment,
    FILM_ID_PK     INTEGER,
    GANRE_ID_PK    INTEGER,
    constraint FILM_GANRES_PK
        primary key (FILM_GANRES_ID),
    constraint FILM_GANRES_FILM_FILM_ID_FK
        foreign key (FILM_ID_PK) references FILM,
    constraint FILM_GANRES_GANRES_GANRE_ID_FK
        foreign key (GANRE_ID_PK) references GANRES
);

create table IF NOT EXISTS LIKES
(
    LIKES_AD   INTEGER auto_increment,
    FILM_ID_PK INTEGER,
    USER_ID_PK INTEGER,
    constraint LIKES_PK
        primary key (LIKES_AD),
    constraint LIKES_FILM_FILM_ID_FK
        foreign key (FILM_ID_PK) references FILM,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID_PK) references USERS
);
