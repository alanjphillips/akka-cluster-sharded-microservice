enablePlugins(JavaAppPackaging)

name := "ClusterShardedApp"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka"         %% "akka-cluster-tools"         % akkaVersion,
  "com.typesafe.akka"         %% "akka-cluster-sharding"      % akkaVersion,
  "com.typesafe.akka"         %% "akka-persistence"           % akkaVersion,
  "com.typesafe.akka"         %% "akka-persistence-cassandra" % "0.18"
)
    