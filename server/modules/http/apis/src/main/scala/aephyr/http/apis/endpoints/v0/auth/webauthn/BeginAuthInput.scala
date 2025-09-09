package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.kernel.types.Base64Url
import aephyr.kernel.jsoniter.given
import aephyr.kernel.tapir.given

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

final case class BeginAuthInput(
  usernameOrEmail: Option[String], // or whatever key you prefer
  userHandle: Option[Base64Url] = None
)

object BeginAuthInput {
  given JsonValueCodec[BeginAuthInput] = JsonCodecMaker.make
  given Schema[BeginAuthInput] = Schema.derived
}
