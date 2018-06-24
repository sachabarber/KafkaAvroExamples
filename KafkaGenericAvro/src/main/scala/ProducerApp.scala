package com.barber.kafka.avro

object ProducerApp extends App {
  private val topic = "avro-demo-topic"
  val producer = new KafkaDemoAvroProducer(topic)
  producer.send()
}
