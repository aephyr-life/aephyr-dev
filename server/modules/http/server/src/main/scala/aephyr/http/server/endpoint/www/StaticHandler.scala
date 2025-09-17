package aephyr.http.server.endpoint.www

import aephyr.http.server.endpoint.HttpTypes.*
import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir.*
import zio.*

import java.nio.charset.StandardCharsets
import scala.io.Source
import java.io.InputStream

object StaticHandler {

  type HtmlEndpoint = PublicEndpoint[Unit, String, String, Caps]

  private val utf8        = StandardCharsets.UTF_8.nn.name.nn
  private def classLoader = getClass.getClassLoader.nn

  private def loadResource(path: String): Task[String] =
    ZIO.attemptBlockingIO {
      classLoader.getResourceAsStream(path) match {
        case is: InputStream =>
          try Source.fromInputStream(is, utf8).mkString
          finally is.close()
        case _ =>
          throw new RuntimeException(s"Resource not found: $path")
      }
    }

  private def zServerHtml[R](endpoint: HtmlEndpoint, resource: String): ZSE[R] =
    endpoint.zServerLogic { _ =>
      loadResource(s"public/$resource").mapError(_.getMessage.nn)
    }

  val testAuthn: ZSE[Any] =
    zServerHtml[Any](StaticContract.testAuthn, "test-webauthn.html")

  val serverEndpoints: List[ZSE[Any]] =
    List(testAuthn)
}
