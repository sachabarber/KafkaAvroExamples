package com.barber.kafka.avro

object PublisherApp extends App {
  private val topic = "avro-specific-demo-topic"
  val producer = new KafkaDemoAvroPublisher(topic)
  producer.send()
}

