package com.barber.kafka.avro

import java.util.Properties
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Collections
import org.apache.kafka.common.errors.TimeoutException
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer

class KafkaDemoAvroSubscriber(val topic:String) {

  private val props = new Properties()
  val groupId = "avro-demo-topic-consumer"
  var shouldRun : Boolean = true

  props.put("bootstrap.servers", "localhost:9092")
  props.put("schema.registry.url", "http://localhost:8081")
  props.put("group.id", groupId)
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "10000")
  props.put("session.timeout.ms", "30000")
  props.put("consumer.timeout.ms", "120000")
  props.put("key.deserializer", classOf[StringDeserializer].getCanonicalName)
  props.put("value.deserializer",classOf[KafkaAvroDeserializer].getCanonicalName)
  //Use Specific Record or else you get Avro GenericRecord.
  props.put("specific.avro.reader", "true")

  private val consumer = new KafkaConsumer[String, com.barber.kafka.avro.User](props)

  def start() = {

    try {
      Runtime.getRuntime.addShutdownHook(new Thread(() => close()))

      consumer.subscribe(Collections.singletonList(topic))

      while (shouldRun) {
        val records: ConsumerRecords[String,  com.barber.kafka.avro.User] = consumer.poll(1000)
        val it = records.iterator()
        while(it.hasNext()) {
          println("Getting message from queue.............")
          val record: ConsumerRecord[String,  com.barber.kafka.avro.User] = it.next()
          val recievedItem =record.value()
          println(s"Saw User ${recievedItem}")
          consumer.commitSync
        }
      }
    }
    catch {
      case timeOutEx: TimeoutException =>
        println("Timeout ")
        false
      case ex: Exception => ex.printStackTrace()
        println("Got error when reading message ")
        false
    }
  }

  def close(): Unit = shouldRun = false
}
