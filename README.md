# freestyle-opscenter

Opscenter

## Generating the proto file
   
`sbt protoGen`

The previous command will overwrite this proto file.

## Requirements

As database, we need install Cassandra. We use this database to store the microservices, nodes and services which we are monitoring.
These tables will be updated directly from each microservices. 

Note: In the next iterations, we will create an image in Docker with all the environment to reproduce it easily.


[comment]: # (Start Copyright)
# Copyright

Freestyle is designed and developed by 47 Degrees

Copyright (C) 2017 47 Degrees. <http://47deg.com>

[comment]: # (End Copyright)