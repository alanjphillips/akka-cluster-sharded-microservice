package com.alaphi.cluster

import scala.concurrent.duration._
import akka.actor.Props
import akka.cluster.Cluster
import akka.cluster.sharding.ShardRegion
import akka.persistence.PersistentActor

case class Command(name: String)

class WorkerActor extends PersistentActor {

  override def persistenceId: String = self.path.parent.name + "-" + self.path.name

  // passivate the entity when no activity
  context.setReceiveTimeout(2.minutes)

  var commands = Vector.empty[Command]

  val cluster = Cluster(context.system)

  def receiveCommand: Receive = {
    case cmd: Command =>
      persist(cmd) { evt =>
        commands :+= cmd
        println("Received " + cmd.name + ": " + cluster.selfUniqueAddress)
      }
  }

  override def receiveRecover: Receive = {
    case cmd: Command => commands :+= cmd
  }

}

object WorkerActor {

  def props(): Props = Props(new WorkerActor)

  val shardName: String = "Worker"

  val numberOfShards = 10

  val idExtractor: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.name, cmd)
  }

  val shardResolver: ShardRegion.ExtractShardId = msg => msg match {
    case cmd: Command   => (math.abs(cmd.name.hashCode) % numberOfShards).toString
  }

}