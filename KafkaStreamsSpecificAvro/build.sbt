import Deps._
import sbt.Keys.scalaVersion

lazy val root = (project in file(".")).
  aggregate(publisher, subscriber, streams).
  settings(
    inThisBuild(List(
      organization := "com.barber",
      scalaVersion := "2.12.1",
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/"
    )),
    name := "scala_kafkastreams_specific_avro_example"
  )

lazy val publisher = (project in file ("publisher")).
  settings(
    name := "publisher",
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
      logBack)
  ).dependsOn(common)
  
lazy val streams = (project in file ("streams")).
  settings(
    name := "streams",
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
	    kafkaClients,
	    kafkaStreams,
	    kafkaStreamsAvroSerializer,
      logBack)
  ).dependsOn(publisher, common)  

lazy val subscriber = (project in file ("subscriber")).
  settings(
    name := "subscriber",
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
      logBack)
  ).dependsOn(streams, common)
  
lazy val common = (project in file ("common")).
  settings(
    name := "common",
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
      logBack)
  )