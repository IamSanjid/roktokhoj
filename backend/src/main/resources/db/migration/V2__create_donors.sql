CREATE SEQUENCE IF NOT EXISTS donors_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE donors
(
    id           BIGINT                NOT NULL,
    name         VARCHAR(255)          NOT NULL,
    password     VARCHAR(255)          NOT NULL,
    email        VARCHAR(255),
    phone        VARCHAR(255)          NOT NULL,
    blood_group  SMALLINT              NOT NULL,
    location     GEOMETRY(Point, 4326) NOT NULL,
    radius       DOUBLE PRECISION      NOT NULL,
    availability SMALLINT              NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_donors PRIMARY KEY (id)
);