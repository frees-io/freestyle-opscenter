CREATE KEYSPACE IF NOT EXISTS freestyle_opscenter WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

DROP TABLE IF EXISTS freestyle_opscenter.services;
DROP TABLE IF EXISTS freestyle_opscenter.microservices;

CREATE TABLE IF NOT EXISTS freestyle_opscenter.services (
id_service uuid,
added_date timestamp,
service text,
id_microservice set<uuid>,
PRIMARY KEY(id_service)
);

CREATE TABLE IF NOT EXISTS freestyle_opscenter.microservices (
id_microservice uuid,
added_date timestamp,
microservice text,
nodes set<text>,
PRIMARY KEY(id_microservice)
);