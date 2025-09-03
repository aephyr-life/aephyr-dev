package aephyr.http.server.util

import zio.http._

object Html {
  private val ct: Headers =
    Headers(Header.Custom("Content-Type", "text/html; charset=utf-8"))

  /** Build an HTML response with the correct Content-Type. */
  def apply(body: String): Response =
    Response.text(body).updateHeaders(_ ++ ct)
}
