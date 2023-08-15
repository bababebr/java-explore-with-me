DROP TABLE IF EXISTS Event, Request, PARTICIPANT_REQUEST, Location, Users, Category;
CREATE TABLE IF NOT EXISTS Event
(
    ID                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    ANNOTATION         VARCHAR                                             NOT NULL,
    CATEGORY           BIGINT,
    CREATED_ON         TIMESTAMP                                           NOT NULL,
    DESCRIPTION        VARCHAR,
    EVENT_DATE         TIMESTAMP                                           NOT NULL,
    INITIATOR          BIGINT                                              NOT NULL,
    LOCATION           BIGINT                                              NOT NULL,
    PAID               BOOL                                                NOT NULL,
    PARTICIPANT_LIMIT  INT                                                 NOT NULL,
    REQUEST_MODERATION BOOL                                                NOT NULL,
    PUBLISHED_DATE TIMESTAMP,
    STATE              VARCHAR                                             NOT NULL,
    TITLE              VARCHAR                                             NOT NULL
);

CREATE TABLE IF NOT EXISTS Request
(
    ID       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    USER_ID  BIGINT                                              NOT NULL,
    EVENT_ID BIGINT                                              NOT NULL,
    STATUS   INT                                                 NOT NULL
);

CREATE TABLE IF NOT EXISTS PARTICIPANT_REQUEST
(
    ID       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    CREATED  TIMESTAMP                                           NOT NULL,
    USER_ID  BIGINT                                              NOT NULL,
    EVENT_ID BIGINT                                              NOT NULL,
    STATUS   VARCHAR                                             NOT NULL
);

CREATE TABLE IF NOT EXISTS Location
(

    ID       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    EVENT_ID BIGINT                                              NOT NULL,
    LAT      FLOAT                                               NOT NULL,
    LON      FLOAT                                               NOT NULL

);

CREATE TABLE IF NOT EXISTS Users
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    EMAIL VARCHAR(254)                                        NOT NULL UNIQUE,
    NAME  VARCHAR(250)                                        NOT NULL
);

CREATE TABLE IF NOT EXISTS Category
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME VARCHAR(50)                                         NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Compilation
(
    ID             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    COMPILATION_ID BIGINT,
    EVENT          BIGINT,
    PINNED         BOOL                                                NOT NULL,
    TITLE          VARCHAR
);