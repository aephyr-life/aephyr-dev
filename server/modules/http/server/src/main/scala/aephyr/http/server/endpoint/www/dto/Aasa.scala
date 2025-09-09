package aephyr.http.server.endpoint.www.dto

import zio.json.JsonCodec
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

final case class Aasa(
  webcredentials: AasaWebCredentials
) derives JsonCodec

object Aasa {
  given JsonValueCodec[Aasa] = JsonCodecMaker.make
}
