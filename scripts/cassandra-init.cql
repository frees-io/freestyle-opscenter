// Keep in mind that we have to change the class replication to NetworkTopologyStrategy in production.
CREATE KEYSPACE IF NOT EXISTS freestyle_opscenter WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

DROP TABLE IF EXISTS freestyle_opscenter.microservices;

CREATE TABLE IF NOT EXISTS freestyle_opscenter.microservices (
id_microservice uuid,
added_date timestamp,
microservice_name text,
nodes set<text>,
service_name text,
added_service_date timestamp,
PRIMARY KEY(id_microservice, added_date, added_service_date)
);