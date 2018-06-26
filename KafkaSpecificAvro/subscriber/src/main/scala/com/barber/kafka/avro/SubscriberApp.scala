package com.barber.kafka.avro

object SubscriberApp extends App {
  private val topic = "avro-specific-demo-topic"

  val consumer = new KafkaDemoAvroSubscriber(topic)
  consumer.start()

}
