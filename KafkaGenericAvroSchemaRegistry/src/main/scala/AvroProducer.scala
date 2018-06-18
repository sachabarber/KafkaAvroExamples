package com.barber.kafka.avro

import java.util.Properties

import com.barber.kafka.avro.models.User
import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory

class AvroProducer {
  val logger = LoggerFactory.getLogger(getClass)

  val kafkaBootstrapServer = sys.env("KAFKA_BOOTSTRAP_SERVER")
  val schemaRegistryUrl = sys.env("SCHEMA_REGISTRY_URL")

  val props = new Properties()
  props.put("bootstrap.servers", kafkaBootstrapServer)
  props.put("schema.registry.url", schemaRegistryUrl)
  props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")
  props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")
  props.put("acks", "1")

  val producer = new KafkaProducer[String, GenericData.Record](props)
  val schemaParser = new Parser

  val key = "key1"
  val valueSchemaJson =
    s"""
    {
      "namespace": "com.avro.junkie",
      "type": "record",
      "name": "User2",
      "fields": [
        {"name": "name", "type": "string"},
        {"name": "favoriteNumber",  "type": "int"},
        {"name": "favoriteColor", "type": "string"}
      ]
    }
  """
  val valueSchemaAvro = schemaParser.parse(valueSchemaJson)
  val avroRecord = new GenericData.Record(valueSchemaAvro)

  val mary = new User("Mary", 840, "Green")
  avroRecord.put("name", mary.name)
  avroRecord.put("favoriteNumber", mary.favoriteNumber)
  avroRecord.put("favoriteColor", mary.favoriteColor)

  def start = {
    try {
      val record = new ProducerRecord("users", key, avroRecord)
      val ack = producer.send(record).get()
      // grabbing the ack and logging for visibility
      logger.info(s"${ack.toString} written to partition ${ack.partition.toString}")
    }
    catch {
      case e: Throwable => logger.error(e.getMessage, e)
    }
  }
}
