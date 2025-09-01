package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.http.apis.types.Base64Url
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

/** Input for starting WebAuthn registration
  * (PublicKeyCredentialCreationOptions).
  */
final case class BeginRegInput(
  userHandle: Option[Base64Url] = None,
  username: Option[String] = None,
  displayName: Option[String] = None,
  authenticatorSelection: Option[AuthenticatorSelection] = None,
  attestation: Option[Attestation] = Some(Attestation.none)
)

object BeginRegInput {
  given JsonValueCodec[BeginRegInput] = JsonCodecMaker.make
  given Schema[BeginRegInput] = Schema.derived
}
