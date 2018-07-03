import akka.actor.ActorSystem
import akka.stream.ActorMaterializer


object AkkaHttpImplicits {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
}
