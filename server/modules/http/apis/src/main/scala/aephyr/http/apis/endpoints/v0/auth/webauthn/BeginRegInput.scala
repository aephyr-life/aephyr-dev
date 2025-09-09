package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.http.apis.endpoints.v0.auth.webauthn.model.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

/** Input for starting WebAuthn registration
  * (PublicKeyCredentialCreationOptions).
  */
final case class BeginRegInput(
  userId: String,
  username: String,
  displayName: String,
  authenticatorSelection: Option[AuthenticatorSelection] = None,
  attestation: Option[Attestation] =
    None,
  //excludeCredentials: Option[List[CredentialDescriptor]] = None
)

object BeginRegInput {
  given JsonValueCodec[BeginRegInput] = JsonCodecMaker.make
  given Schema[BeginRegInput] = Schema.derived
}
