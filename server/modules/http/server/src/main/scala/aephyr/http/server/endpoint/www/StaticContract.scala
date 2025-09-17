package aephyr.http.server.endpoint.www

import sttp.tapir.*

object StaticContract {

  val routes = List(StaticHandler.testAuthn)

  private def html(path: String): PublicEndpoint[Unit, String, String, Any] = {
    import Codec.string.format
    import CodecFormat.TextHtml
    endpoint.get
      .in(path)
      .out(stringBodyUtf8AnyFormat(format(TextHtml())))
      .errorOut(stringBody)
  }

  val testAuthn: PublicEndpoint[Unit, String, String, Any] =
    html("test-authn.html")
}
