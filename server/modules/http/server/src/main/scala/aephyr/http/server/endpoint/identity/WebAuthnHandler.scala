package aephyr.http.server.endpoint.identity

import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.http.apis.endpoints.v0.auth.webauthn.{BeginRegOutput, WebAuthnApi}
import aephyr.http.apis.types.RawJson
import sttp.tapir.ztapir.*
import aephyr.http.server.endpoint.HttpTypes.*
import aephyr.http.server.mapping.webauthn.WebAuthnDtoMapper
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

object WebAuthnHandler {

  private type WebAuthnEnv = WebAuthnService

  final case class Top(publicKey: Option[RawJson] = None)

  given JsonValueCodec[Top] = JsonCodecMaker.make

  def publicKeyRawFrom(clientJson: String): RawJson = {
    val top = readFromString[Top](clientJson)
    top.publicKey.getOrElse(RawJson(clientJson))
  }

  val registrationOptions: ZSE[WebAuthnEnv] =
    WebAuthnApi.registrationOptions.zServerLogic { in =>
     for {
       reg <- WebAuthnService.registrationOptions().mapError(WebAuthnDtoMapper.toProblem)
     } yield BeginRegOutput(reg.tx, publicKeyRawFrom(reg.clientJson))
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

  val serverEndpoints: List[ZServerEndpoint[WebAuthnEnv, Caps]] =
    List(registrationOptions)
}
