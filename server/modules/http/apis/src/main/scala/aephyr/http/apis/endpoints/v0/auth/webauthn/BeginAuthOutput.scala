package aephyr.http.apis.endpoints.v0.auth.webauthn

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

import aephyr.http.apis.endpoints.v0.auth.webauthn.model.CredDescriptor
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.UserVerification
import aephyr.kernel.types.Base64Url
import aephyr.kernel.jsoniter.given
import aephyr.kernel.tapir.given

import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

final case class BeginAuthOutput(
  challenge: Base64Url,
  timeout: Option[Long] = None,
  rpId: Option[String] = None,
  allowCredentials: Option[List[CredDescriptor]] = None,
  userVerification: Option[UserVerification] = None,
  extensions: Option[Map[String, String]] = None,
  tx: String
)

object BeginAuthOutput {
  given JsonValueCodec[BeginAuthOutput] = JsonCodecMaker.make
  given Schema[BeginAuthOutput] = Schema.derived
}
