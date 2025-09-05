package aephyr.http.server.endpoint

import zio.http.{Response, Routes}
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import sttp.tapir.server.interceptor.log.DefaultServerLog
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import aephyr.http.server.endpoint.ops.DocsHandler
import aephyr.auth.application.webauthn.WebAuthnService
import zio.*

object HttpRoutes {

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

  private val serverOptions: ZioHttpServerOptions[Any] = {
    val log = DefaultServerLog(
      doLogWhenReceived = msg => ZIO.logDebug(msg),
      doLogWhenHandled = (msg, ex) => ex.fold(ZIO.logDebug(msg))(e => ZIO.logWarning(msg) *> ZIO.logDebug(s"${e.getMessage}")),
      doLogAllDecodeFailures = msg => ZIO.logDebug(msg),
      noLog = ZIO.unit
    )
    ZioHttpServerOptions.default
      .copy(serverLog = log)
      .prependInterceptor(CORSInterceptor.default) // add more interceptors here
  }

  private val interpreter = ZioHttpInterpreter(serverOptions)

  // ---- Interpret endpoint lists --------------------------------------------

  private val publicRoutes: Routes[Any, Response] =
    interpreter.toHttp(HttpHandler.public)

  private val identityRoutes: Routes[HttpHandler.IdentityEnv, Response] =
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
  val routes: Routes[HttpHandler.Env, Response] =
    widenAny[HttpHandler.Env](publicRoutes) ++
      widenEnv[HttpHandler.Env, HttpHandler.IdentityEnv](identityRoutes) ++
      widenAny[HttpHandler.Env](docsRoutes)
}
