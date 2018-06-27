package com.barber.kafka.avro

import java.util.{Properties, UUID}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

class KafkaDemoAvroPublisher(val topic:String) {

  private val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("schema.registry.url", "http://localhost:8081")
  props.put("key.serializer", classOf[StringSerializer].getCanonicalName)
  props.put("value.serializer",classOf[KafkaAvroSerializer].getCanonicalName)
  props.put("client.id", UUID.randomUUID().toString())

  private val producer =   new KafkaProducer[String,User](props)
  private val producerBad =   new KafkaProducer[String,UserWithoutName](props)

  def send(): Unit = {
    try {
      val rand =  new scala.util.Random(44343)

      for(i <- 1 to 10) {
        val id = rand.nextInt()
        val itemToSend = User(id , "ishot.com")
        println(s"Producer sending data ${itemToSend.toString}")
        producer.send(new ProducerRecord[String, User](topic, itemToSend))
        producer.flush()
      }




      //these should fail since they do not match the current Topic Schema (User Avro file/schema)
      println("Sending some bad 'UserWithoutName' which should break against current topc Schema")
      for(i <- 1 to 10) {
        val id = rand.nextInt()
        val itemToSend = UserWithoutName(id)
        println(s"Producer sending data ${itemToSend.toString}")
        producerBad.send(new ProducerRecord[String, UserWithoutName](topic, itemToSend))
        producerBad.flush()
      }


    } catch {
      case ex: Exception =>
        println(ex.printStackTrace().toString)
        ex.printStackTrace()
    }
  }
}