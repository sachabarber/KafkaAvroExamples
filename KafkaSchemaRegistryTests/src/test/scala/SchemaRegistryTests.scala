import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import Matchers._

class SchemaRegistryTests
  extends FlatSpec
  with BeforeAndAfter
  with Matchers {

  before {
  }

  after {
  }


  //https://stackoverflow.com/questions/32315789/akka-httpresponse-read-body-as-string-scala
  //https://sachabarbs.wordpress.com/2017/09/01/madcap-idea-part-8-intermediate-step-rest-api-for-interactive-kafka-stream-ktable-queries/


  "Testing" should "be easy" in {
    assert(1 === 1)
    1 shouldEqual 1
  }


}
