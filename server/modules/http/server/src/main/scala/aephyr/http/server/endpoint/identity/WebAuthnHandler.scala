package aephyr.http.server.endpoint.identity

import aephyr.adapters.db.UserWriteRepository
import aephyr.api.shared.Problem
import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.auth.ports.JwtIssuer
import aephyr.http.apis.endpoints.v0.auth.webauthn.{BeginRegOutput, JwtOut, WebAuthnApi, WebAuthnErrorDto}
import aephyr.http.apis.types.RawJson
import sttp.tapir.ztapir.*
import aephyr.http.server.endpoint.HttpTypes.*
import aephyr.http.server.mapping.webauthn.WebAuthnDtoMapper
import aephyr.http.server.wiring.identity.WebAuthnLayers
import aephyr.identity.application.ports.UserWritePort
import aephyr.kernel.PersistenceError
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import zio.ZIO

object WebAuthnHandler {

  final case class Top(publicKey: Option[RawJson] = None)

  given JsonValueCodec[Top] = JsonCodecMaker.make

  def publicKeyRawFrom(clientJson: String): RawJson = {
    val top = readFromString[Top](clientJson)
    top.publicKey.getOrElse(RawJson(clientJson))
  }

  val registrationOptions: ZSE[WebAuthnLayers.Env] =
    WebAuthnApi.registrationOptions.zServerLogic { in =>
     for {
       reg <- WebAuthnService.registrationOptions().mapError(WebAuthnDtoMapper.toProblem)
     } yield BeginRegOutput(reg.tx, publicKeyRawFrom(reg.clientJson))
    }

  private def dbToProblem(e: PersistenceError): Problem[WebAuthnErrorDto] =
    Problem(e.getMessage.nn)

  val registrationVerify: ZSE[WebAuthnLayers.Env] =
    WebAuthnApi.registrationVerify.zServerLogic {
      case (tx, json) =>
        val jsonString = json.value // TODO string?
        for {
          verified <- WebAuthnService.registrationVerify(tx, jsonString).mapError(WebAuthnDtoMapper.toProblem)
          roles     = Set.empty[String]
          now <- ZIO.clockWith(_.instant)
          _   <- UserWritePort
            .upsertActive(verified.userId, now)
            .mapError(dbToProblem)
            .unit
          tuple <- JwtIssuer.mintAccess(verified.userId, roles)
          (token, ttlSeconds) = tuple
        } yield JwtOut(token, "Bearer", ttlSeconds)
    }

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

  val serverEndpoints: List[ZSE[WebAuthnLayers.Env]] =
    List(registrationOptions, registrationVerify)
}
