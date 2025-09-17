package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.auth.domain.AuthTx
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import aephyr.http.apis.types.RawJson
import sttp.tapir.Schema
import sttp.tapir.generic.auto.*
import aephyr.http.apis.types.AuthTxCodecs.given

final case class BeginAuthOutput(tx: AuthTx, publicKey: RawJson)

object BeginAuthOutput {
  given JsonValueCodec[BeginAuthOutput] = JsonCodecMaker.make
  given Schema[BeginAuthOutput] = Schema.derived
}
