/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.barber.kafka.avro

import scala.annotation.switch
import scala.io.Source

case class UserWithUUID(var id: Int, var name: String, var uuid: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0, "","")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case pos if pos == 0 => {
        id
      }.asInstanceOf[AnyRef]
      case pos if pos == 1 => {
        name
      }.asInstanceOf[AnyRef]
      case pos if pos == 2 => {
        uuid
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case pos if pos == 0 => this.id = {
        value
      }.asInstanceOf[Int]
      case pos if pos == 1 => this.name = {
        value.toString
      }.asInstanceOf[String]
      case pos if pos == 2 => this.uuid = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = UserWithUUID.SCHEMA$
}

object UserWithUUID {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse(
    Source.fromURL(getClass.getResource("/userWithUUIDSchema.avsc")).mkString)
}
