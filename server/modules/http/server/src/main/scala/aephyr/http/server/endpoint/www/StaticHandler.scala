package aephyr.http.server.endpoint.www

import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir.*
import zio.{ZIO, Task}

import java.nio.charset.StandardCharsets
import scala.io.Source
import java.io.InputStream

import sttp.tapir.ztapir.*

object StaticHandler {
  
  val testAuthn: ZServerEndpoint[Nothing, Any] = zServerHtml(
    StaticContract.testAuthn, "test-webauthn.html"
  )
  
  val serverEndpoints: List[ZServerEndpoint[Any, Any]] =
    List(testAuthn)

  type HtmlEndpoint =  PublicEndpoint[Unit, String, String, Any]
  private val utf8 = StandardCharsets.UTF_8.nn.name.nn
  private def classLoader = getClass.getClassLoader.nn

  private def loadResource(path: String): Task[String] =
    ZIO.attemptBlockingIO {
      classLoader.getResourceAsStream(path) match {
        case is: InputStream => try {
          Source.fromInputStream(is, utf8).mkString
        } finally is.close()
        case _ => throw new RuntimeException()
      }
    }

  private def zServerHtml(endpoint: HtmlEndpoint, resource: String): ZServerEndpoint[Nothing, Any] =
    endpoint.zServerLogic { _ =>
      loadResource(s"public/$resource")
        .mapError(_.getMessage.nn)
    }
}
