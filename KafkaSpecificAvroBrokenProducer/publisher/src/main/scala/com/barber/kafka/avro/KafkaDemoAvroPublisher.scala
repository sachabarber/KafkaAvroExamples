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
  private val producerUserWithoutName =   new KafkaProducer[String,UserWithoutName](props)
  private val producerUserWithBooleanIdBad =   new KafkaProducer[String,UserWithBooleanId](props)
  private val producerAnotherExampleWithStringIdBad = new KafkaProducer[String,AnotherExampleWithStringId](props)

  def send(): Unit = {
    try {
      val rand =  new scala.util.Random(44343)

      //we expect this to work, as its the one that is going to define the Avro format of then topic
      //since its the 1st published message on the topic (Assuming you have not preregistered the topic key + Avro schemea
      //with the schema registry already)
      for(i <- 1 to 10) {
        val id = rand.nextInt()
        val itemToSend = User(id , "ishot.com")
        println(s"Producer sending data ${itemToSend.toString}")
        producer.send(new ProducerRecord[String, User](topic, itemToSend))
        producer.flush()
      }


      //we expect this to work as having a User without a name is ok, as Name is a "string" so can be empty
      for(i <- 1 to 10) {
        val id = rand.nextInt()
        val itemToSend = UserWithoutName(id)
        println(s"Producer sending data ${itemToSend.toString}")
        producerUserWithoutName.send(new ProducerRecord[String, UserWithoutName](topic, itemToSend))
        producerUserWithoutName.flush()
      }


      //we expect this to fail as its trying to send a different incompatible Avro object on the topic
      //which is currently using the "User" (Avro object / Schema)
      sendBadProducerValue("UserWithBooleanId", () => {
        val itemToSend = UserWithBooleanId(true)
        println(s"Producer sending data ${itemToSend.toString}")
        producerUserWithBooleanIdBad.send(new ProducerRecord[String, UserWithBooleanId](topic, itemToSend))
        producerUserWithBooleanIdBad.flush()
      })

      //we expect this to fail as its trying to send a different incompatible Avro object on the topic
      //which is currently using the "User" (Avro object / Schema)
      sendBadProducerValue("AnotherExampleWithStringId", () => {
        val itemToSend = AnotherExampleWithStringId("fdfdfdsdsfs")
        println(s"Producer sending data ${itemToSend.toString}")
        producerAnotherExampleWithStringIdBad.send(new ProducerRecord[String, AnotherExampleWithStringId](topic, itemToSend))
        producerAnotherExampleWithStringIdBad.flush()
      })

    } catch {
      case ex: Exception =>
        println(ex.printStackTrace().toString)
        ex.printStackTrace()
    }
  }

  def sendBadProducerValue(itemType: String, produceCallback: () => Unit) : Unit = {
    try {
      //we expect this to fail as its trying to send a different incompatible
      // Avro object on the topic which is currently using the "User" (Avro object / Schema)
      println(s"Sending $itemType")
      produceCallback()
    } catch {
      case ex: Exception => {
        println("==============================================================================\r\n")
        println(s" We were expecting this due to incompatble '$itemType' item being sent\r\n")
        println("==============================================================================\r\n")
        println(ex.printStackTrace().toString)
        println()
        ex.printStackTrace()
      }
    }
  }
}