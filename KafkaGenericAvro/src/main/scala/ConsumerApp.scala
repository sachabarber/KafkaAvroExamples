import com.barber.kafka.avro.KafkaDemoAvroConsumer

object ConsumerApp extends App {
  private val topic = "avro-demo-topic"

  val consumer = new KafkaDemoAvroConsumer(topic)
  consumer.start()

}
