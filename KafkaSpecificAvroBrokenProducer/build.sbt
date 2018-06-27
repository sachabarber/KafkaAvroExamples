import Deps._
import sbt.Keys.scalaVersion

lazy val root = (project in file(".")).
  aggregate(publisher, subscriber).
  settings(
    inThisBuild(List(
      organization := "com.barber",
      scalaVersion := "2.12.1",
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/"
    )),
    name := "scala_kafka_specific_avro_example"
  )

lazy val publisher = (project in file ("publisher")).
  settings(
    name := "publisher",
    /* sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue, */
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
      logBack)
  ).dependsOn(common)

lazy val subscriber = (project in file ("subscriber")).
  settings(
    name := "subscriber",
    /* sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue, */
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
      logBack)
  ).dependsOn(publisher, common)
  
  lazy val common = (project in file ("common")).
  settings(
    name := "common",
    libraryDependencies ++= Seq(
      kafka,
      avro,
      avroSerializer,
      logBack)
  )