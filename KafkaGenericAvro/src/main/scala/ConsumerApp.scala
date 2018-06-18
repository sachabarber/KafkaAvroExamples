import com.barber.kafka.avro.KafkaDemoAvroConsumer
import com.barber.kafka.avro.models.User

object ConsumerApp extends App {
  private val topic = "avro-demo-topic"

  val producer = new KafkaDemoAvroConsumer(topic)

}
