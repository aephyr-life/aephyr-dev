package aephyr.api

import aephyr.kernel.telemetry.TraceId
import shared.*
import aephyr.security.Bearer
import sttp.tapir.*
import sttp.tapir.json.jsoniter.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import sttp.tapir.model.ServerRequest

object TapirDsl extends Tapir {

  val traceIdIn: EndpointInput[Option[TraceId]] =
    header[Option[String]]("X-Trace-Id")
      .description("Correlation / trace id")
      .map(_.map(TraceId.apply))(_.map(_.value))

  val ipAddressIn: EndpointInput[Option[IpAddress]] =
    extractFromRequest { (sr: ServerRequest) =>
      sr.connectionInfo.remote.map(addr => IpAddress(addr.toString))
    }.description("Client IP address")

  val userAgentIn: EndpointInput[Option[UserAgent]] =
    header[Option[String]]("User-Agent")
      .map(_.map(UserAgent.apply))(_.map(_.value))
      .description("User agent")

  val requestCtxIn: EndpointInput[RequestContext] =
    (traceIdIn and ipAddressIn and userAgentIn).mapTo[RequestContext]

  val bearerIn: EndpointInput[Option[Bearer]] =
    auth.bearer[String]()
      .description("Bearer access token")
      .map(s => Option.when(s.nonEmpty)(Bearer(s)))(_.map(_.value).getOrElse(""))

  val authCtxIn: EndpointInput[AuthenticationContext] =
    (bearerIn and requestCtxIn).mapTo[AuthenticationContext]

  def jsonIn[T: Schema : JsonValueCodec]: EndpointInput[T] = jsonBody[T]

  def jsonOut[T: Schema : JsonValueCodec]: EndpointOutput[T] = jsonBody[T]

  def base[A: Schema : JsonValueCodec]: PublicEndpoint[Unit, Problem[A], Unit, Any] =
    endpoint
      .in("api")
      .errorOut(jsonBody[Problem[A]])

  def secureBaseBearer[A: Schema : JsonValueCodec]
    : Endpoint[AuthenticationContext, Unit, Problem[A], Unit, Any] =
    base[A]
      .securityIn(authCtxIn)

}
