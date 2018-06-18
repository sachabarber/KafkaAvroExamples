package com.barber.kafka.avro

import com.barber.kafka.avro.models.User

object ProducerApp extends App {
  private val topic = "avro-demo-topic"

  val producer = new KafkaDemoAvroProducer(topic)
  val user1 = User(1, "sacha barber", None)
  val user2 = User(2, "eva mendes", Some("eva@ishot.com"))
  producer.send(List(user1, user2))
}
