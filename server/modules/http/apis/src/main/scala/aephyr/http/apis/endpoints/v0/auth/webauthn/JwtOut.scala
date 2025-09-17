package aephyr.http.apis.endpoints.v0.auth.webauthn

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

final case class JwtOut(
  accessToken: String,
  tokenType: String = "Bearer",
  expiresInSec: Long
)

object JwtOut {
  given JsonValueCodec[JwtOut] = JsonCodecMaker.make
  given Schema[JwtOut] = Schema.derived
}
