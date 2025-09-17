package aephyr.http.apis.endpoints.v0.auth.webauthn

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

import sttp.tapir.Schema

sealed trait WebAuthnErrorDto

object WebAuthnErrorDto {

  case object InvalidChallenge extends WebAuthnErrorDto

  case object NotRegistered extends WebAuthnErrorDto

  final case class ServerError(msg: String) extends WebAuthnErrorDto

  given JsonValueCodec[WebAuthnErrorDto] =
    JsonCodecMaker
      .make(
        CodecMakerConfig
          .withDiscriminatorFieldName(Some("type"))
      )

  given Schema[WebAuthnErrorDto] = Schema.derived
}
