package com.alaphi.cluster

import akka.actor.ActorSystem
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.typesafe.config.ConfigFactory


object Boot extends App {

  val config1 = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=2551").
    withFallback(ConfigFactory.load())

  val config2 = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=2552").
    withFallback(ConfigFactory.load())

  val system1 = ActorSystem("ClusterSystem", config1)
  val system2 = ActorSystem("ClusterSystem", config2)

  ClusterSharding(system1).start(
    typeName = WorkerActor.shardName,
    entityProps = WorkerActor.props(),
    settings = ClusterShardingSettings(system1),
    extractEntityId = WorkerActor.idExtractor,
    extractShardId = WorkerActor.shardResolver)

  val workerRegion = ClusterSharding(system1).shardRegion(WorkerActor.shardName)

  system1.actorOf(WorkDispatcherActor.props(workerRegion))

}
