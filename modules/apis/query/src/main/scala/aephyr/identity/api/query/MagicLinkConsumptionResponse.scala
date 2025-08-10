package aephyr.identity.api.query

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

final case class MagicLinkConsumptionResponse()

object MagicLinkConsumptionResponse:
  implicit val codec: JsonValueCodec[MagicLinkConsumptionResponse] =
    JsonCodecMaker.make[MagicLinkConsumptionResponse](CodecMakerConfig)
    
