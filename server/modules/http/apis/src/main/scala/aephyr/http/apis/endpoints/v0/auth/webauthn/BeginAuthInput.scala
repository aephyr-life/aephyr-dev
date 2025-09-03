package aephyr.http.apis.endpoints.v0.auth.webauthn

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import aephyr.http.apis.types.Base64Url

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
