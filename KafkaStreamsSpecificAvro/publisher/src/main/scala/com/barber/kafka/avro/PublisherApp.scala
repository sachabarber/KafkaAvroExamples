package com.barber.kafka.avro

object PublisherApp extends App {
  private val topic = "avro-streams-input-topic"
  val producer = new KafkaDemoAvroPublisher(topic)
  producer.send()
}

