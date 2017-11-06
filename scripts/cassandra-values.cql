INSERT INTO freestyle_opscenter.microservices (id_microservice, added_date, microservice, nodes)
VALUES (
5b6962dd-3f90-4c93-8f61-eabfa4a803e2,
1382655211694,
'Microservice 1',
{'node-1', 'node-2', 'node-3'}
);

INSERT INTO freestyle_opscenter.microservices (id_microservice, added_date, microservice, nodes)
VALUES (
5b6962dd-3f90-4c93-8f61-eabfa4a804e2,
1392655211694,
'Microservice 2',
{'node-9891', 'node-13891', 'node-233'}
);

INSERT INTO freestyle_opscenter.services (id_service, added_date, service, id_microservice)
VALUES (
5b6961dd-3f90-4c93-8f61-eabfa4a304e2,
1392645111694,
'Kafka',
{5b6962dd-3f90-4c93-8f61-eabfa4a804e2,5b6962dd-3f90-4c93-8f61-eabfa4a803e2}
);

INSERT INTO freestyle_opscenter.services (id_service, added_date, service, id_microservice)
VALUES (
5b6961dd-3f90-4c93-8f61-eabfa4a201e2,
1392645111624,
'Cassandra',
{5b6962dd-3f90-4c93-8f61-eabfa4a804e2,5b6962dd-3f90-4c93-8f61-eabfa4a803e2}
);