package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.http.apis.types.RawJson
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema
import sttp.tapir.generic.auto.*

final case class VerifyRegInput(
  tx: String, // TODO make opaque type
  credential: RawJson
)

object VerifyRegInput {
  given JsonValueCodec[VerifyRegInput] = JsonCodecMaker.make
  given Schema[VerifyRegInput] = Schema.derived
}
