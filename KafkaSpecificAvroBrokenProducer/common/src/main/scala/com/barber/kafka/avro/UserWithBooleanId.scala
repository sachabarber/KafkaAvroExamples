/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.barber.kafka.avro

import scala.annotation.switch
import scala.io.Source

case class UserWithBooleanId(var id: Boolean) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(false)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case pos if pos == 0 => {
        id
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case pos if pos == 0 => this.id = {
        value
      }.asInstanceOf[Boolean]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = UserWithBooleanId.SCHEMA$
}

object UserWithBooleanId {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse(
    Source.fromURL(getClass.getResource("/userWithBooleanIdSchema.avsc")).mkString)
}