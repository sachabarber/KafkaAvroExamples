import scala.io.Source
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import scala.concurrent._
import scala.concurrent.duration._
import akka.http.scaladsl.model._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.http.scaladsl.unmarshalling.Unmarshal
import AkkaHttpImplicits.{executionContext, materializer, system}

object RegistryApp extends App {

  var payload = ""
  var result = ""
  val schemaregistryMediaType = MediaType.custom("application/vnd.schemaregistry.v1+json",false)
  implicit  val c1 = ContentType(schemaregistryMediaType, () => HttpCharsets.`UTF-8`)

  //These queries are the same ones found here, but instead of using curl I am using Akka Http
  //https://docs.confluent.io/current/schema-registry/docs/intro.html


  //  # Register a new version of a schema under the subject "Kafka-key"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-key/versions
  //  {"id":1}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/subjects/Kafka-key/versions")
  println("Register a new version of a schema under the subject \"Kafka-key\"")
  println(result)


  //  # Register a new version of a schema under the subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-value/versions
  //  {"id":1}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/subjects/Kafka-value/versions")
  println("Register a new version of a schema under the subject \"Kafka-value\"")
  println(result)


  //  # List all subjects
  //  $ curl -X GET http://localhost:8081/subjects
  //    ["Kafka-value","Kafka-key"]
  result = get("http://localhost:8081/subjects")
  println("List all subjects")
  println(result)

  //  # Fetch a schema by globally unique id 1
  //  $ curl -X GET http://localhost:8081/schemas/ids/1
  //  {"schema":"\"string\""}
  result = get("http://localhost:8081/schemas/ids/1")
  println("Fetch a schema by globally unique id 1")
  println(result)



  //  # List all schema versions registered under the subject "Kafka-value"
  //  $ curl -X GET http://localhost:8081/subjects/Kafka-value/versions
  //    [1]
  //
  //  # Fetch version 1 of the schema registered under subject "Kafka-value"
  //  $ curl -X GET http://localhost:8081/subjects/Kafka-value/versions/1
  //  {"subject":"Kafka-value","version":1,"id":1,"schema":"\"string\""}
  //
  //  # Deletes version 1 of the schema registered under subject "Kafka-value"
  //  $ curl -X DELETE http://localhost:8081/subjects/Kafka-value/versions/1
  //  1
  //
  //  # Register the same schema under the subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-value/versions
  //  {"id":1}
  //
  //  # Deletes the most recently registered schema under subject "Kafka-value"
  //  $ curl -X DELETE http://localhost:8081/subjects/Kafka-value/versions/latest
  //  2
  //
  //  # Register the same schema under the subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-value/versions
  //  {"id":1}
  //
  //  # Fetch the schema again by globally unique id 1
  //  $ curl -X GET http://localhost:8081/schemas/ids/1
  //  {"schema":"\"string\""}
  //
  //  # Check whether a schema has been registered under subject "Kafka-key"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-key
  //  {"subject":"Kafka-key","version":3,"id":1,"schema":"\"string\""}
  //
  //  # Test compatibility of a schema with the latest schema under subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/compatibility/subjects/Kafka-value/versions/latest
  //  {"is_compatible":true}
  //
  //  # Get top level config
  //    $ curl -X GET http://localhost:8081/config
  //    {"compatibilityLevel":"BACKWARD"}
  //
  //  # Update compatibility requirements globally
  //    $ curl -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"compatibility": "NONE"}' \
  //  http://localhost:8081/config
  //    {"compatibility":"NONE"}
  //
  //  # Update compatibility requirements under the subject "Kafka-value"
  //  $ curl -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"compatibility": "BACKWARD"}' \
  //  http://localhost:8081/config/Kafka-value
  //  {"compatibility":"BACKWARD"}
  //
  //  # Deletes all schema versions registered under the subject "Kafka-value"
  //  $ curl -X DELETE http://localhost:8081/subjects/Kafka-value
  //    [3]
  //
  //  # List all subjects
  //  $ curl -X GET http://localhost:8081/subjects
  //    ["Kafka-key"]





  private[RegistryApp] def post(data: String, url:String)(implicit contentType:ContentType): String = {
    val responseFuture: Future[HttpResponse] =
      Http(system).singleRequest(
        HttpRequest(
          HttpMethods.POST,url,
          entity = HttpEntity(contentType, data.getBytes())
        )
      )
    val html = Await.result(responseFuture.flatMap(x => Unmarshal(x.entity).to[String]), 5 seconds)
    html
  }


  private[RegistryApp] def get(url:String)(implicit contentType:ContentType): String = {
    val responseFuture: Future[HttpResponse] = Http(system).singleRequest(HttpRequest(HttpMethods.GET,url))
    val html = Await.result(responseFuture.flatMap(x => Unmarshal(x.entity).to[String]), 5 seconds)
    html
  }



}
