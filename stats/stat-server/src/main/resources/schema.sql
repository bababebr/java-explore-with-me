/**
  TODO IP field CHECK(ipv4 regex)
 */
DROP TABLE IF EXISTS service_hits;
CREATE TABLE IF NOT EXISTS service_hits (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    APP VARCHAR(64) NOT NULL,
    URI VARCHAR(128) NOT NULL,
    IP VARCHAR(15) NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL
);