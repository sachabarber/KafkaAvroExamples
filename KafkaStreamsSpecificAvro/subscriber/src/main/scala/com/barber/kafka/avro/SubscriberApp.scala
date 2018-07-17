package com.barber.kafka.avro

object SubscriberApp extends App {
  private val topic = "avro-streams-useruuid-output-topic"

  val consumer = new KafkaDemoAvroSubscriber(topic)
  consumer.start()

}
