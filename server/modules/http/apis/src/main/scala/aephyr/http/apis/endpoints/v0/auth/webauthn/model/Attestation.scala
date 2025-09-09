package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

enum Attestation derives CanEqual:
  case none, indirect, direct, enterprise

object Attestation {
  given JsonValueCodec[Attestation] = JsonCodecMaker.make
  given Schema[Attestation]         = Schema.derived
}
