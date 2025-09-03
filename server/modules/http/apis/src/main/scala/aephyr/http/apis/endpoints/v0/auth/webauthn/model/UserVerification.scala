package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

enum UserVerification:
  case required, preferred, discouraged

object UserVerification {
  given JsonValueCodec[UserVerification] = JsonCodecMaker.make
  given Schema[UserVerification]         = Schema.derived
}
