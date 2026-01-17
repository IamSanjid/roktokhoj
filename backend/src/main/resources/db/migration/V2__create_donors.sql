CREATE SEQUENCE IF NOT EXISTS donors_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE donors
(
    id          BIGINT NOT NULL,
    name        VARCHAR(255),
    email       VARCHAR(255),
    phone       VARCHAR(255),
    blood_group SMALLINT,
    location    GEOMETRY(Point, 4326),
    radius      DOUBLE PRECISION,
    CONSTRAINT pk_donors PRIMARY KEY (id)
);