package aephyr.http.apis.endpoints.v0.auth.webauthn

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import aephyr.kernel.types.Base64Url
import aephyr.kernel.jsoniter.given
import aephyr.kernel.tapir.given

import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

final case class FinishRegInput(
  tx: String, // same as in BeginRegOutput
  id: Base64Url, // credentialId from navigator.credentials.create
  rawId: Base64Url, // typically same as id
  clientDataJSON: Base64Url, // base64url
  attestationObject: Base64Url, // base64url
  transports: Option[List[String]] = None,
  label: Option[String] = None
)

object FinishRegInput {
  given JsonValueCodec[FinishRegInput] = JsonCodecMaker.make
  given Schema[FinishRegInput] = Schema.derived
}
