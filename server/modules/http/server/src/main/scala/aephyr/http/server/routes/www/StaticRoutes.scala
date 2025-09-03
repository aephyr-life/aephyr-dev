package aephyr.http.server.routes.www

import sttp.tapir.ztapir._
import zio.*
import java.nio.charset.StandardCharsets

object StaticRoutes {

  private def loadResource(path: String): Task[String] =
    ZIO.attemptBlockingIO {
      getClass.getClassLoader.nn.getResourceAsStream(path) match {
        case is: java.io.InputStream =>
          try {
            scala.io.Source.fromInputStream(is, StandardCharsets.UTF_8.nn.name.nn).mkString
          } finally is.close()
        case _ => throw new RuntimeException()
      }
    }

  private def ep(path: String, resource: String): ZServerEndpoint[Any, Any] = {
    val api = endpoint.get
      .in(path)
      .out(stringBody)
      .out(header[String]("Content-Type"))
      .errorOut(stringBody)
    
    api.zServerLogic { _ =>
      StaticRoutes.loadResource(s"public/$resource")
        .map(html => (html, "text/html; charset=utf-8"))
        .mapError(_.getMessage.nn)
    }
  } 
  
  val endpoints = List(
    ep("test-webauthn.html", "test-webauthn.html")
  )
  
//  private val eTest = endpoint.get
//    .in("test-webauthn.html")
//    .out(stringBody)
//    .out(header[String]("Content-Type"))
//    .errorOut(stringBody)
//
//  val testEp: ZServerEndpoint[Any, Any] =
//    eTest.zServerLogic { _ =>
//      StaticRoutes.loadResource("public/test-webauthn.html")
//        .map(html => (html, "text/html; charset=utf-8"))
//        .mapError(_.getMessage.nn)
//    }
}
