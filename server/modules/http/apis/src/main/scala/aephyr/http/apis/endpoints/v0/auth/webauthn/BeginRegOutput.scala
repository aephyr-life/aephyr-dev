package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.http.apis.types.Base64Url

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

import aephyr.http.apis.endpoints.v0.auth.webauthn.model.CredDescriptor
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.AuthenticatorSelection
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.Attestation
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.RpEntity
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.UserEntity
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.PubKeyCredParam

final case class BeginRegOutput(
  rp: RpEntity,
  user: UserEntity,
  challenge: Base64Url,
  pubKeyCredParams: List[PubKeyCredParam],
  timeout: Option[Long] = None, // milliseconds
  excludeCredentials: Option[List[CredDescriptor]] = None,
  authenticatorSelection: Option[AuthenticatorSelection] = None,
  attestation: Option[Attestation] = Some(Attestation.none),
  extensions: Option[Map[String, String]] = None,
  tx: String // your correlation id
)

object BeginRegOutput {
  given JsonValueCodec[BeginRegOutput] = JsonCodecMaker.make
  given Schema[BeginRegOutput] = Schema.derived
}
