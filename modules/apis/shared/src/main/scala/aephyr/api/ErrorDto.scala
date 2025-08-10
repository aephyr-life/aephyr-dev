package aephyr.api

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.*

final case class ErrorDto(
                           code: String,
                           message: String,
                           status: Int,
                           details: Option[Map[String, String]] = None,
                           traceId: Option[String] = None
                         )

object ErrorDto:
  given JsonValueCodec[ErrorDto] = JsonCodecMaker.make

