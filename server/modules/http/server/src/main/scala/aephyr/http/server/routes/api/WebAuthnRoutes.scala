package aephyr.http.server.routes.api

import scala.language.unsafeNulls
import java.util.UUID

import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.model.StatusCode
import sttp.tapir.ztapir.*
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import zio.*
import zio.http.{Response, Routes}

import aephyr.http.apis.endpoints.v0.auth.webauthn.WebAuthnApi.*
import aephyr.http.apis.Problem
import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.auth.application.webauthn.WebAuthnService.*
import aephyr.auth.domain.webauthn.*
import aephyr.identity.domain.User

object WebAuthnRoutes {

  // The HTTP layer only needs the service
  type Env = WebAuthnService

  private def toProblem(e: WebAuthnError): Problem[WebAuthnError] =
    Problem( // TODO better problem
      "unknown type",
      "unkown title",
      500,
      None,
      None,
      None,
      Some(e)
    )

  val routes: Routes[Env, Response] = {
    // POST /api/.../registration/options
//    val beginRegEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
//      eBeginReg.zServerLogic { in =>
//        val cmd = BeginRegCmd(
//          userId = User.Id(UUID.fromString(in.userId)),
//          username = in.username,
//          displayName = in.displayName,
//          authenticatorSelection = in.authenticatorSelection,
//          attestation = in.attestation.getOrElse(AttestationConveyance.None),
//          excludeCredentials = in.excludeCredentials.getOrElse(Nil)
//        )
//
//        WebAuthnService
//          .beginRegistration(cmd)
//          .map(res => BeginRegOutput(optionsJson = res.optionsJson, tx = res.tx))
//          .mapError(toProblem)
//      }

    // POST /api/.../registration/verify
//    val finishRegEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
//      eFinishReg.zServerLogic { in =>
//        val cmd = FinishRegCmd(tx = in.tx, responseJson = in.responseJson, label = in.label)
//        WebAuthnService
//          .finishRegistration(cmd)
//          .as(StatusCode.Ok)
//          .mapError(toProblem)
//      }

    // POST /api/.../authentication/options
//    val beginAuthEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
//      eBeginAuth.zServerLogic { in =>
//        val cmd = BeginAuthCmd(
//          username = in.username,
//          allowCredentials = in.allowCredentials.getOrElse(Nil),
//          userVerification = in.userVerification
//        )
//        WebAuthnService
//          .beginAuthentication(cmd)
//          .map(res => BeginAuthOutput(optionsJson = res.optionsJson, tx = res.tx))
//          .mapError(toProblem)
//      }

    // POST /api/.../authentication/verify
//    val finishAuthEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
//      eFinishAuth.zServerLogic { in =>
//        val cmd = FinishAuthCmd(tx = in.tx, responseJson = in.responseJson)
//        WebAuthnService
//          .finishAuthentication(cmd)
//          .as(StatusCode.Ok -> "ok")
//          .mapError(toProblem)
//      }

    val serverOptions: ZioHttpServerOptions[Env] =
      ZioHttpServerOptions.default.prependInterceptor(CORSInterceptor.default)

    ZioHttpInterpreter[Env](serverOptions).toHttp(
//      List(beginRegEp, finishRegEp, beginAuthEp, finishAuthEp)
      List.empty
    )
  }
}
