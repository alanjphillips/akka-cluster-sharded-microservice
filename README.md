Akka Cluster Sharded Microservice
=================================
Basic use of Akka Cluster Sharding with Cassandra configured for persistence. Messages are consumed from Kafka and persisted to Cassandra using akka-persistence

Install docker, docker-machine and docker-compose

1) Connect to 'default' machine, see docker docs on how to create machine in virtualbox

akka-http-kafka-streams> eval "$(docker-machine env default)"

2) CD into project and use SBT to build and publish to local Docker repo:

ClusterShardedApp> sbt clean docker:publishLocal

3) Run docker compose to launch Cluster Sharded App and Cassandra which is used for persistence

ClusterShardedApp> docker-compose up

4) Connect to Cassandra and view persisted messages

>  cqlsh 192.168.99.100

cqlsh> describe tables

cqlsh> select * from akka.messages;