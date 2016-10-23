AKKA CLUSTER SHARDED Microservice
=================================

Install docker and docker-compose

1) CD into project and use SBT to build and publish to local Docker repo:

ClusterShardedApp> sbt clean docker:publishLocal

2) Run docker compose to launch Cluster Sharded App and Cassandra which is used for persistence

ClusterShardedApp> docker-compose up
