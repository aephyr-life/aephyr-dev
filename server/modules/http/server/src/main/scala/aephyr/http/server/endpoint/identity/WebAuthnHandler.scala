package aephyr.http.server.endpoint.identity

import aephyr.api.shared.Problem
import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.auth.domain.AuthTx
import aephyr.auth.domain.webauthn.WebAuthnError
import aephyr.auth.ports.JwtIssuer
import aephyr.http.apis.endpoints.v0.auth.webauthn.{
  BeginAuthOutput,
  BeginRegOutput,
  JwtOut,
  WebAuthnApi,
  WebAuthnErrorDto
}
import aephyr.http.apis.types.RawJson
import sttp.tapir.ztapir.*
import aephyr.http.server.endpoint.HttpTypes.*
import aephyr.http.server.mapping.webauthn.WebAuthnDtoMapper
import aephyr.http.server.wiring.identity.WebAuthnLayers
import aephyr.identity.application.ports.UserWritePort
import aephyr.kernel.PersistenceError
import aephyr.kernel.id.UserId
import com.github.plokhotnyuk.jsoniter_scala.core.{
  readFromString,
  JsonValueCodec
}
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
    WebAuthnApi.registrationOptions.zServerLogic {
      in =>
        for {
          reg <- WebAuthnService
            .registrationOptions()
            .mapError(WebAuthnDtoMapper.toProblem)
        } yield BeginRegOutput(reg.tx, publicKeyRawFrom(reg.clientJson))
    }

  private def dbToProblem(e: PersistenceError): Problem[WebAuthnErrorDto] =
    Problem(e.getMessage.nn)

  val registrationVerify: ZSE[WebAuthnLayers.Env] =
    WebAuthnApi.registrationVerify.zServerLogic {
      case (tx: AuthTx, json) =>
        verifyAndMint(
          WebAuthnService
            .registrationVerify(tx, json.value)
            .map(_.userId)
        )
    }

  val authenticationOptions: ZSE[WebAuthnLayers.Env] =
    WebAuthnApi.authenticationOptions.zServerLogic {
      _ =>
        WebAuthnService
          .authenticationOptions()
          .mapError(WebAuthnDtoMapper.toProblem)
          .map(
            res => BeginAuthOutput(res.tx, publicKeyRawFrom(res.clientJson))
          )
    }

  val authenticationVerify: ZSE[WebAuthnLayers.Env] =
    WebAuthnApi.authenticationVerify.zServerLogic {
      case (tx: AuthTx, json) =>
        verifyAndMint(
          WebAuthnService
            .authenticationVerify(tx, json.value)
            .map(_.userId)
        )
    }

  private def verifyAndMint(
    ioUserId: ZIO[WebAuthnLayers.Env, WebAuthnError, UserId]
  ): ZIO[WebAuthnLayers.Env, Problem[WebAuthnErrorDto], JwtOut] =
    for {
      userId <- ioUserId.mapError(WebAuthnDtoMapper.toProblem)
      now <- ZIO.clockWith(_.instant)
      _ <- UserWritePort.upsertActive(userId, now).mapError(dbToProblem).unit
      roles = Set.empty[String]
      tuple <- JwtIssuer.mintAccess(userId, roles)
      (token, ttlSeconds) = tuple
    } yield JwtOut(token, "Bearer", ttlSeconds)

  val serverEndpoints: List[ZSE[WebAuthnLayers.Env]] =
    List(registrationOptions, registrationVerify, authenticationOptions, authenticationVerify)
}
