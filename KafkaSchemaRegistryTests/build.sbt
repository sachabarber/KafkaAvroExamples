name := "KafkaSchemaRegistryTests"

version := "1.0"

scalaVersion := "2.12.1"


resolvers ++= Seq(
  Classpaths.typesafeReleases,
  "confluent" at "http://packages.confluent.io/maven/",
  Resolver.mavenLocal
)

libraryDependencies ++= Seq(

  "org.apache.kafka"  %   "kafka_2.11"            % "1.1.0",
  "org.apache.avro"   %   "avro"                  % "1.8.2",
  "io.confluent"      %   "kafka-avro-serializer" % "3.2.1",
  "ch.qos.logback"    %   "logback-classic"       % "1.1.7",
  "org.scalatest"     %%  "scalatest"             % "3.0.5"     % Test,
  "com.typesafe.akka" %%  "akka-http"             % "10.1.3",
  "com.typesafe.akka" %%  "akka-http-spray-json"  % "10.1.3",
  "io.spray"          %%  "spray-json"            % "1.3.4",
  "com.typesafe.akka" %%  "akka-actor"            % "2.5.13",
  "com.typesafe.akka" %%  "akka-stream"           % "2.5.13"
)