package aephyr.api.shared

import sttp.tapir.Schema
import sttp.tapir.generic.auto.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

final case class Problem[A](
  `type`: String,
  title: String,
  status: Int,
  detail: Option[String] = None,
  traceId: Option[String] = None,
  instance: Option[String] = None,
  error: Option[A] = None
)

object Problem {

  given [A](using JsonValueCodec[A]): JsonValueCodec[Problem[A]] =
    JsonCodecMaker.make

  given [A: Schema]: Schema[Problem[A]] =
    Schema.derived
}
