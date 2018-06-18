package com.barber.kafka.avro

import java.util.Properties

import com.barber.kafka.avro.models.User
import org.apache.avro.Schema
import org.apache.avro.io.DatumReader
import org.apache.avro.io.Decoder
import org.apache.avro.specific.SpecificDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.DecoderFactory
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.TimeoutException
import java.util.Collections

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords

import scala.io.Source
import scala.util.{Failure, Success, Try}


class KafkaDemoAvroConsumer(val topic:String) {

  private val props = new Properties()
  val groupId = "demo-topic-consumer"
  val schemaString = Source.fromURL(getClass.getResource("/userSchema.avsc")).mkString
  val schema: Schema = new Schema.Parser().parse(schemaString)
  var shouldRun : Boolean = true

  props.put("group.id", groupId)
  props.put("zookeeper.connect", "localhost:2181")
  props.put("auto.offset.reset", "smallest")
  props.put("consumer.timeout.ms", "120000")
  props.put("auto.commit.interval.ms", "10000")

  private val consumer = new KafkaConsumer[String, Array[Byte]](props)


  def start() = {

    try {

      consumer.subscribe(Collections.singletonList(topic))

      while (shouldRun) {
        val records: ConsumerRecords[String, Array[Byte]] = consumer.poll(1000)
        val it = records.iterator()
        while(it.hasNext()) {
          println("Getting message from queue.............")
          val record: ConsumerRecord[String, Array[Byte]] = it.next()

          val user = parseUser(record.value())
          println(s"Saw User ${user}")
          consumer.commitSync
        }
      }
    }
    catch {
      case timeOutEx: TimeoutException =>
        false
      case ex: Exception => ex.printStackTrace()
        println("Got error when reading message ")
        false
    }
  }

  private def parseUser(message: Array[Byte]): Option[User] = {

    // Deserialize and create generic record
    val reader: DatumReader[GenericRecord] = new SpecificDatumReader[GenericRecord](schema)
    val decoder: Decoder = DecoderFactory.get().binaryDecoder(message, null)
    val userData: GenericRecord = reader.read(null, decoder)

    // Make user object
    val finalUser = Try[User](
      User(userData.get("id").toString.toInt, userData.get("name").toString, try {
        Some(userData.get("email").toString)
      } catch {
        case _ => None
      })
    )

    finalUser match {
      case Success(u) =>
        Some(u)
      case Failure(e) =>
        None
    }
  }

  def close(): Unit = shouldRun = false

}