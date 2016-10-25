package com.alaphi.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.Future

class WorkDispatcherActor(workerRegion : ActorRef) extends Actor with ActorLogging {
  implicit val system = context.system
  implicit val materialiser = ActorMaterializer()

  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("kafka:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val done =
      Consumer.committableSource(consumerSettings, Subscriptions.topics("worker_commands"))
        .mapAsync(1) { msg =>
          log.info(s"Received message from kafka: $msg")
          workerRegion ! Command(msg.record.value)
          Future.successful(msg)
        }
        .mapAsync(1) { msg =>
          msg.committableOffset.commitScaladsl()
        }
        .runWith(Sink.ignore)
  }

}

object WorkDispatcherActor {
   def props(workerRegion : ActorRef) : Props = {
     Props(new WorkDispatcherActor(workerRegion))
   }
}
