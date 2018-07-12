package com.barber.kafka.avro

import java.util.{Properties, UUID}

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.ProducerConfig


class KafkaDemoAvroPublisher(val topic:String) {

  private val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put("schema.registry.url", "http://localhost:8081")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,classOf[KafkaAvroSerializer].getCanonicalName)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString())
  props.put(ProducerConfig.ACKS_CONFIG,  "all")
  props.put(ProducerConfig.RETRIES_CONFIG, "0")
  private val producer =   new KafkaProducer[String,User](props)

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
    } catch {
      case ex: Exception =>
        println(ex.printStackTrace().toString)
        ex.printStackTrace()
    }
  }
}