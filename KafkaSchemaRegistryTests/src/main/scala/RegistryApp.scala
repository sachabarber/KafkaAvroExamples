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
  val schemaRegistryMediaType = MediaType.custom("application/vnd.schemaregistry.v1+json",false)
  implicit  val c1 = ContentType(schemaRegistryMediaType, () => HttpCharsets.`UTF-8`)

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
  println("EXPECTING {\"id\":1}")
  println(s"GOT ${result}\r\n")


  //  # Register a new version of a schema under the subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-value/versions
  //  {"id":1}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/subjects/Kafka-value/versions")
  println("Register a new version of a schema under the subject \"Kafka-value\"")
  println("EXPECTING {\"id\":1}")
  println(s"GOT ${result}\r\n")


  //  # List all subjects
  //  $ curl -X GET http://localhost:8081/subjects
  //    ["Kafka-value","Kafka-key"]
  result = get("http://localhost:8081/subjects")
  println("List all subjects")
  println("EXPECTING [\"Kafka-value\",\"Kafka-key\"]")
  println(s"GOT ${result}\r\n")


  //  # Fetch a schema by globally unique id 1
  //  $ curl -X GET http://localhost:8081/schemas/ids/1
  //  {"schema":"\"string\""}
  result = get("http://localhost:8081/schemas/ids/1")
  println("Fetch a schema by globally unique id 1")
  println("EXPECTING {\"schema\":\"\\\"string\\\"\"}")
  println(s"GOT ${result}\r\n")


  //  # List all schema versions registered under the subject "Kafka-value"
  //  $ curl -X GET http://localhost:8081/subjects/Kafka-value/versions
  //    [1]
  result = get("http://localhost:8081/subjects/Kafka-value/versions")
  println("List all schema versions registered under the subject \"Kafka-value\"")
  println("EXPECTING [1]")
  println(s"GOT ${result}\r\n")


  //  # Fetch version 1 of the schema registered under subject "Kafka-value"
  //  $ curl -X GET http://localhost:8081/subjects/Kafka-value/versions/1
  //  {"subject":"Kafka-value","version":1,"id":1,"schema":"\"string\""}
  result = get("http://localhost:8081/subjects/Kafka-value/versions/1")
  println("Fetch version 1 of the schema registered under subject \"Kafka-value\"")
  println("EXPECTING {\"subject\":\"Kafka-value\",\"version\":1,\"id\":1,\"schema\":\"\\\"string\\\"\"}")
  println(s"GOT ${result}\r\n")


  //  # Deletes version 1 of the schema registered under subject "Kafka-value"
  //  $ curl -X DELETE http://localhost:8081/subjects/Kafka-value/versions/1
  //  1
  result = delete("http://localhost:8081/subjects/Kafka-value/versions/1")
  println("Deletes version 1 of the schema registered under subject \"Kafka-value\"")
  println("EXPECTING 1")
  println(s"GOT ${result}\r\n")


  //  # Register the same schema under the subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-value/versions
  //  {"id":1}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/subjects/Kafka-value/versions")
  println("Register the same schema under the subject \"Kafka-value\"")
  println("EXPECTING {\"id\":1}")
  println(s"GOT ${result}\r\n")


  //  # Deletes the most recently registered schema under subject "Kafka-value"
  //  $ curl -X DELETE http://localhost:8081/subjects/Kafka-value/versions/latest
  //  2
  result = delete("http://localhost:8081/subjects/Kafka-value/versions/latest")
  println("Deletes the most recently registered schema under subject \"Kafka-value\"")
  println("EXPECTING 2")
  println(s"GOT ${result}\r\n")


  //  # Register the same schema under the subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-value/versions
  //  {"id":1}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/subjects/Kafka-value/versions")
  println("Register the same schema under the subject \"Kafka-value\"")
  println("EXPECTING {\"id\":1}")
  println(s"GOT ${result}\r\n")


  //  # Fetch the schema again by globally unique id 1
  //  $ curl -X GET http://localhost:8081/schemas/ids/1
  //  {"schema":"\"string\""}
  result = get("http://localhost:8081/schemas/ids/1")
  println("Fetch the schema again by globally unique id 1")
  println("EXPECTING {\"schema\":\"\\\"string\\\"\"}")
  println(s"GOT ${result}\r\n")


  //  # Check whether a schema has been registered under subject "Kafka-key"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/subjects/Kafka-key
  //  {"subject":"Kafka-key","version":1,"id":1,"schema":"\"string\""}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/subjects/Kafka-key")
  println("Check whether a schema has been registered under subject \"Kafka-key\"")
  println("EXPECTING {\"subject\":\"Kafka-key\",\"version\":1,\"id\":1,\"schema\":\"\\\"string\\\"\"}")
  println(s"GOT ${result}\r\n")


  //  # Test compatibility of a schema with the latest schema under subject "Kafka-value"
  //  $ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"schema": "{\"type\": \"string\"}"}' \
  //  http://localhost:8081/compatibility/subjects/Kafka-value/versions/latest
  //  {"is_compatible":true}
  payload = Source.fromURL(getClass.getResource("/simpleStringSchema.avsc")).mkString
  result = post(payload,"http://localhost:8081/compatibility/subjects/Kafka-value/versions/latest")
  println("Test compatibility of a schema with the latest schema under subject \"Kafka-value\"")
  println("EXPECTING {\"is_compatible\":true}")
  println(s"GOT ${result}\r\n")


  //  # Get top level config
  //    $ curl -X GET http://localhost:8081/config
  //    {"compatibilityLevel":"BACKWARD"}
  result = get("http://localhost:8081/config")
  println("Get top level config")
  println("EXPECTING {\"compatibilityLevel\":\"BACKWARD\"}")
  println(s"GOT ${result}\r\n")


  //  # Update compatibility requirements globally
  //    $ curl -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"compatibility": "NONE"}' \
  //  http://localhost:8081/config
  //    {"compatibility":"NONE"}
  payload = Source.fromURL(getClass.getResource("/compatibilityNONE.json")).mkString
  result = put(payload,"http://localhost:8081/config")
  println("Update compatibility requirements globally")
  println("EXPECTING {\"compatibility\":\"NONE\"}")
  println(s"GOT ${result}\r\n")


  //  # Update compatibility requirements under the subject "Kafka-value"
  //  $ curl -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  //  --data '{"compatibility": "BACKWARD"}' \
  //  http://localhost:8081/config/Kafka-value
  //  {"compatibility":"BACKWARD"}
  payload = Source.fromURL(getClass.getResource("/compatibilityBACKWARD.json")).mkString
  result = put(payload,"http://localhost:8081/config/Kafka-value")
  println("Update compatibility requirements under the subject \"Kafka-value\"")
  println("EXPECTING {\"compatibility\":\"BACKWARD\"}")
  println(s"GOT ${result}\r\n")


  //  # Deletes all schema versions registered under the subject "Kafka-value"
  //  $ curl -X DELETE http://localhost:8081/subjects/Kafka-value
  //    [3]
  result = delete("http://localhost:8081/subjects/Kafka-value")
  println("Deletes all schema versions registered under the subject \"Kafka-value\"")
  println("EXPECTING [3]")
  println(s"GOT ${result}\r\n")

  //  # List all subjects
  //  $ curl -X GET http://localhost:8081/subjects
  //    ["Kafka-key"]
  result = get("http://localhost:8081/subjects")
  println("List all subjects")
  println("EXPECTING [\"Kafka-key\"]")
  println(s"GOT ${result}\r\n")


  private[RegistryApp] def post(data: String, url:String)(implicit contentType:ContentType): String = {
    sendData(data, url, HttpMethods.POST, contentType)
  }

  private[RegistryApp] def put(data: String, url:String)(implicit contentType:ContentType): String = {
    sendData(data, url, HttpMethods.PUT, contentType)
  }

  private[RegistryApp] def sendData(data: String, url:String, method:HttpMethod, contentType:ContentType): String = {
    val responseFuture: Future[HttpResponse] =
      Http(system).singleRequest(
        HttpRequest(
          method,
          url,
          entity = HttpEntity(contentType, data.getBytes())
        )
      )
    val html = Await.result(responseFuture.flatMap(x => Unmarshal(x.entity).to[String]), 5 seconds)
    html
  }

  private[RegistryApp] def get(url:String)(implicit contentType:ContentType): String = {
    noBodiedRequest(url, HttpMethods.GET, contentType)
  }

  private[RegistryApp] def delete(url:String)(implicit contentType:ContentType): String = {
    noBodiedRequest(url, HttpMethods.DELETE, contentType)
  }

  private[RegistryApp] def noBodiedRequest(url:String,method:HttpMethod, contentType:ContentType): String = {
    val responseFuture: Future[HttpResponse] = Http(system).singleRequest(HttpRequest(method,url))
    val html = Await.result(responseFuture.flatMap(x => Unmarshal(x.entity).to[String]), 5 seconds)
    html
  }
}
