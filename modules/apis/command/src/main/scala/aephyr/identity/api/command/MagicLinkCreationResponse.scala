package aephyr.identity.api.command

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

final case class MagicLinkCreationResponse(message: String)

object MagicLinkCreationResponse:
  implicit val codec: JsonValueCodec[MagicLinkCreationResponse] =
    JsonCodecMaker.make[MagicLinkCreationResponse](CodecMakerConfig)
