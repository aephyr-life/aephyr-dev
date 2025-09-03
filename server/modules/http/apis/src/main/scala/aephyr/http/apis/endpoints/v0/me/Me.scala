package aephyr.http.apis.endpoints.v0.me

import sttp.tapir.Schema
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

final case class Me(
                        id: String,
                        username: String,
                        displayName: String
                      )

object Me {
  given JsonValueCodec[Me] = JsonCodecMaker.make
  given Schema[Me]         = Schema.derived
}
