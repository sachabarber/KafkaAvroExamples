package com.barber.kafka.avro

import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory


object ProducerApp extends App {

  BasicConfigurator.configure()
  val logger = LoggerFactory.getLogger(getClass)
  logger.info("Starting the application")

  val producer = new AvroProducer
  producer.start
}
