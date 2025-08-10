package aephyr.identity.api.query

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

// TODO rename to MagicLinkAccepted
final case class MagicLinkConsumptionResponse(message: String)

object MagicLinkConsumptionResponse:
  implicit val codec: JsonValueCodec[MagicLinkConsumptionResponse] =
    JsonCodecMaker.make[MagicLinkConsumptionResponse](CodecMakerConfig)
    
