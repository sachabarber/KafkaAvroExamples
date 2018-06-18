name := "KafkaGenericAvro"

version := "1.0"

scalaVersion := "2.12.1"


resolvers ++= Seq(
  Classpaths.typesafeReleases,
  "confluent" at "http://packages.confluent.io/maven/",
  Resolver.mavenLocal
)

libraryDependencies ++= Seq(

  "org.apache.kafka" % "kafka_2.11" % "1.1.0",
  "org.apache.avro" % "avro" % "1.8.2",
  "io.confluent" % "kafka-avro-serializer" % "3.2.1",
  "ch.qos.logback" %  "logback-classic" % "1.1.7"
)