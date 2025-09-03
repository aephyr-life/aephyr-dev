package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

// WebAuthn COSE algorithm id (e.g. -7 = ES256, -257 = RS256)
final case class PubKeyCredParam(`type`: String = "public-key", alg: Int)

object PubKeyCredParam {
  given JsonValueCodec[PubKeyCredParam] = JsonCodecMaker.make
  given Schema[PubKeyCredParam]         = Schema.derived
}
