CREATE DATABASE jooq;
CREATE SCHEMA public;
CREATE TABLE persons(
    id bigint,
    uid varchar(255),
    created timestamp,
    modified timestamp,
    name varchar(255)
);