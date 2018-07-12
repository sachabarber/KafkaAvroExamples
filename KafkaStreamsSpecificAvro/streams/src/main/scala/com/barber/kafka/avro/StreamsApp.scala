package com.barber.kafka.avro


object StreamsApp extends App {
  private val inputTopic = "avro-streams-input-topic"
  private val outputTopic = "avro-streams-output-topic"

  val consumer = new KafkaDemoAvroStreams(inputTopic, outputTopic)
  consumer.start()

}
