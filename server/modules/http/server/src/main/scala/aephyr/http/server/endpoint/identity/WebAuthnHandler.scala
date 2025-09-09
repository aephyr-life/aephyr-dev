package aephyr.http.server.endpoint.identity

import aephyr.api.shared.Problem
import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.auth.application.webauthn.WebAuthnService.{BeginRegCmd, BeginRegResult}
import aephyr.auth.domain
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.{Attestation, AuthenticatorSelection, CredDescriptor, PubKeyCredParam, RpEntity, UserEntity}
import aephyr.http.apis.endpoints.v0.auth.webauthn.{BeginRegInput, BeginRegOutput, WebAuthnApi, WebAuthnErrorDto, model}
import sttp.tapir.ztapir.*
import aephyr.http.server.endpoint.HttpTypes.*
import aephyr.http.server.endpoint.identity.WebAuthnHandler.beginRegCmd
import aephyr.http.server.mapping.webauthn.WebAuthnDtoMapper
import aephyr.http.server.security.AuthService
import aephyr.kernel.codec.Base64UrlCodec
import aephyr.kernel.id.UserId
import aephyr.kernel.types.Base64Url
import zio.{IO, ZIO}
import aephyr.kernel.types.Base64Url.*

object WebAuthnHandler {

  private type WebAuthnEnv = WebAuthnService
  
  val registrationOptions: ZSE[WebAuthnEnv] =
    WebAuthnApi.registrationOptions.zServerLogic { in =>
     for {
       cmd <- WebAuthnDtoMapper.beginRegCmd(in)
       reg <- WebAuthnService.beginRegistration(cmd).mapError(WebAuthnDtoMapper.toProblem)
       res <- WebAuthnDtoMapper.beginRegOut(reg)
     } yield res
    }

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

  val serverEndpoints: List[ZServerEndpoint[WebAuthnEnv, Caps]] =
    List()
}
