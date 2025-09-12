package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.auth.domain.AuthTx
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema
import sttp.tapir.generic.auto.*
import aephyr.http.apis.types.RawJson
import aephyr.http.apis.types.AuthTxCodecs.given

final case class BeginRegOutput(
  tx: AuthTx,
  publicKey: RawJson
)

object BeginRegOutput {
  given JsonValueCodec[BeginRegOutput] = JsonCodecMaker.make
  given Schema[BeginRegOutput] = Schema.derived
}
