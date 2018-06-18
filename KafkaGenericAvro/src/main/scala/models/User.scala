package com.barber.kafka.avro.models

case class User(id: Int, name: String, email: Option[String])