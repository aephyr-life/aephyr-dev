package aephyr.http.server.endpoint.www.dto

import zio.json.JsonCodec
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

final case class AasaWebCredentials(
  apps: List[String]
) derives JsonCodec

object AasaWebCredentials {
  given JsonValueCodec[AasaWebCredentials] = JsonCodecMaker.make
}
