package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

final case class AuthenticatorSelection(
  authenticatorAttachment: Option[String] =
    None, // "platform" | "cross-platform"
  residentKey: Option[String] =
    None, // "required" | "preferred" | "discouraged"
  userVerification: Option[UserVerification] = None,
  requireResidentKey: Option[Boolean] = None // legacy web compat
)

object AuthenticatorSelection {
  given JsonValueCodec[AuthenticatorSelection] = JsonCodecMaker.make
  given Schema[AuthenticatorSelection] = Schema.derived
}
