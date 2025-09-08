package aephyr.http.server.endpoint

import zio.*
import zio.http.* // Routes, Response, etc.

import sttp.tapir.ztapir.* // .widen, ZServerEndpoint, etc.
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}

import aephyr.http.server.endpoint.ops.DocsHandler

object HttpRoutes {
  import HttpTypes.* // Caps, ZSE, Env

  // 1) Server options (CORS)
  private val serverOptions: ZioHttpServerOptions[Any] =
    ZioHttpServerOptions.default.prependInterceptor(CORSInterceptor.default)

  // 2) Interpreter
  private val interpreter = ZioHttpInterpreter(serverOptions)

  // 3) Widen endpoints to Env first (type-safe), then interpret to zio-http Routes
  private val publicZseEnv: List[ZSE[Env]]   = HttpHandler.public.map(_.widen[Env])
  private val identityZseEnv: List[ZSE[Env]] = HttpHandler.identity.map(_.widen[Env])

  private val publicRoutes: Routes[Env, Response]   = interpreter.toHttp(publicZseEnv)
  private val identityRoutes: Routes[Env, Response] = interpreter.toHttp(identityZseEnv)

  // 4) Docs (these need Any)
  private val docsRoutes: Routes[Any, Response] = {
    val plain = HttpHandler.documented.map(_.endpoint) ++ HttpHandler.identity.map(_.endpoint)
    interpreter.toHttp(DocsHandler.fromEndpoints(plain))
  }

  // 5) Combine. Cast docs (Any) to Env is safe: it needs nothing.
  val routes: Routes[Env, Response] =
    publicRoutes ++ identityRoutes ++ docsRoutes.asInstanceOf[Routes[Env, Response]]
}
