CREATE TABLE IF NOT EXISTS Event
(
    ID                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    ANNOTATION         VARCHAR                                             NOT NULL,
    CATEGORY           BIGINT                                              NOT NULL,
    CREATED_ON         TIMESTAMP                                           NOT NULL,
    DESCRIPTION        VARCHAR,
    EVENT_DATE         TIMESTAMP                                           NOT NULL,
    INITIATOR          BIGINT                                              NOT NULL,
    LOCATION           BIGINT                                              NOT NULL,
    PAID               BOOL                                                NOT NULL,
    PARTICIPANT_LIMIT  INT                                                 NOT NULL,
    REQUEST_MODERATION BOOL                                                NOT NULL,
    STATE              INT                                                 NOT NULL,
    TITLE              VARCHAR                                             NOT NULL,

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
    EMAIL VARCHAR(64)                                         NOT NULL UNIQUE,
    NAME  VARCHAR(128)                                        NOT NULL
);

CREATE TABLE IF NOT EXISTS Category
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME VARCHAR(50)                                         NOT NULL UNIQUE
);