package aephyr.http.apis.endpoints.v0.auth.webauthn

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

import sttp.tapir.Schema

sealed trait WebAuthnError

object WebAuthnError {

  case object InvalidChallenge extends WebAuthnError

  case object NotRegistered extends WebAuthnError

  case object ServerError extends WebAuthnError

  given JsonValueCodec[WebAuthnError] =
    JsonCodecMaker
      .make(
        CodecMakerConfig
          .withDiscriminatorFieldName(Some("type"))
      )

  given Schema[WebAuthnError] = Schema.derived
}
