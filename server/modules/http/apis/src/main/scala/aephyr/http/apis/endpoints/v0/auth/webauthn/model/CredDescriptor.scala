package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import aephyr.http.apis.types.Base64Url

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

/** WebAuthn PublicKeyCredentialDescriptor See:
  * https://www.w3.org/TR/webauthn-2/#dictdef-publickeycredentialdescriptor
  *
  *   - `type` is always "public-key" for WebAuthn
  *   - `id` is the credential ID (byte sequence) → we send as base64url-encoded
  *     string
  *   - `transports` is optional; values like "usb" | "nfc" | "ble" | "internal"
  *     \| "hybrid"
  */
final case class CredDescriptor(
  `type`: String = "public-key",
  id: Base64Url,
  transports: Option[List[String]] = None
)

object CredDescriptor {
  given JsonValueCodec[CredDescriptor] = JsonCodecMaker.make
  given Schema[CredDescriptor] = Schema.derived
}
