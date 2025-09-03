package aephyr.http.apis

import sttp.tapir.*
import sttp.tapir.json.jsoniter.*

import com.github.plokhotnyuk.jsoniter_scala.core.*

object TapirDsl extends Tapir {

  val traceId: EndpointInput[Option[String]] =
    header[Option[String]]("X-Trace-Id")
      .description("Correlation / trace id")

  def jsonIn[T: Schema : JsonValueCodec]: EndpointInput[T] = jsonBody[T]

  def jsonOut[T: Schema : JsonValueCodec]: EndpointOutput[T] = jsonBody[T]

  def base[A: Schema : JsonValueCodec]: PublicEndpoint[Unit, Problem[A], Unit, Any] =
    endpoint
      .in("api")
      .errorOut(jsonBody[Problem[A]])
}
