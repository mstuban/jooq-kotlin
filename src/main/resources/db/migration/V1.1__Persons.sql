CREATE SEQUENCE IF NOT EXISTS persons_seq INCREMENT 1 START 1 MINVALUE 1;
CREATE TABLE IF NOT EXISTS persons(
                        id bigint not null default nextval('persons_seq'),
                        uid varchar(500),
                        created timestamp,
                        modified timestamp,
                        name varchar(500)
);