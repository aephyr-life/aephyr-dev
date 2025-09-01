package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

final case class RpEntity(
                           name: String,
                           id: Option[String] = None
                         )
object RpEntity {
  given JsonValueCodec[RpEntity] = JsonCodecMaker.make
  given Schema[RpEntity]         = Schema.derived
}
