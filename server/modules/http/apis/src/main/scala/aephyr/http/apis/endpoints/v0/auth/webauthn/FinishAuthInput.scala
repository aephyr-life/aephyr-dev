package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.http.apis.types.Base64Url

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

final case class FinishAuthInput(
  tx: String,
  id: Base64Url,
  rawId: Base64Url,
  clientDataJSON: Base64Url,
  authenticatorData: Base64Url,
  signature: Base64Url,
  userHandle: Option[Base64Url] = None
)
object FinishAuthInput {
  given JsonValueCodec[FinishAuthInput] = JsonCodecMaker.make
  given Schema[FinishAuthInput] = Schema.derived
}
