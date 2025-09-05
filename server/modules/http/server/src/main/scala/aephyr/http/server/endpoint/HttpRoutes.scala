package aephyr.http.server.endpoint

import zio.*
import zio.http.{Response, Routes}
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import sttp.tapir.server.interceptor.cors.CORSInterceptor

import aephyr.http.server.endpoint.ops.DocsHandler

object HttpRoutes {

  import HttpTypes.*
  
  // ---- Helpers --------------------------------------------------------------

  /**
   * Widen Routes[Any, _] to any richer environment.
   * Safe because it needs nothing.
   */
  private def widenAny[R](r: Routes[Any, Response]): Routes[R, Response] =
    r.asInstanceOf[Routes[R, Response]]

  /**
   * Widen a specific env to a richer super-env (intersection type).
   */
  private def widenEnv[R1, R2 <: R1](r: Routes[R2, Response]): Routes[R1, Response] =
    r.asInstanceOf[Routes[R1, Response]]

  // Keep server options simple; CORS is global. (Custom ServerLog APIs vary by Tapir version.)
  private val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.default.prependInterceptor(CORSInterceptor.default)

  private val interpreter = ZioHttpInterpreter(serverOptions)

  // ---- Interpret endpoint lists --------------------------------------------

  private val publicRoutes: Routes[PublicEnv, Response] =
    interpreter.toHttp(HttpHandler.public)

  private val identityRoutes: Routes[IdentityEnv, Response] =
    interpreter.toHttp(HttpHandler.identity)

  /**
   * Docs from *plain* endpoints (only those you mark as documented + identity)
   */
  private val docsRoutes: Routes[Any, Response] = {
    val plain =
      HttpHandler.documented.map(_.endpoint) ++
        HttpHandler.identity.map(_.endpoint)
    interpreter.toHttp(DocsHandler.fromEndpoints(plain))
  }

  // ---- Final combined router -----------------------------------------------

  /**
   * All HTTP routes of the server, requiring the union environment `Env`.
   */
  val routes: Routes[Env, Response] =
    widenEnv[Env, HttpHandler.PublicEnv](publicRoutes) ++
      widenEnv[Env, HttpHandler.IdentityEnv](identityRoutes) ++
      widenAny[Env](docsRoutes)
}
