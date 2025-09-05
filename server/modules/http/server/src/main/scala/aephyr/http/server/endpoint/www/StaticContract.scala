package aephyr.http.server.endpoint.www

import sttp.tapir.*

object StaticContract {

  val routes = List(StaticHandler.testAuthn)
  
  private def html(path: String): PublicEndpoint[Unit, String, String, Any] =
    endpoint.get
      .in(path)
      .out(stringBodyUtf8AnyFormat(CodecFormat.TextHtml()))
      .errorOut(stringBody)

  val testAuthn: PublicEndpoint[Unit, String, String, Any] =
    html("test-authn.html")
}
