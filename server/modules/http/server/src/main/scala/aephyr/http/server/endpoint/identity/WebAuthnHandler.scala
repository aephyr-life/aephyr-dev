package aephyr.http.server.endpoint.identity

import aephyr.auth.application.webauthn.WebAuthnService

import sttp.tapir.ztapir.*

object WebAuthnHandler {
  type Env = WebAuthnService

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
  
  val serverEndpoints: List[ZServerEndpoint[Env, Any]] =
    List()
}
