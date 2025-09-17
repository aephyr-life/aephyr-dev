package aephyr.http.apis

import sttp.tapir.Schema
import sttp.tapir.generic.auto.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

@Deprecated
final case class Problem[A](
                             `type`: String,
                             title: String,
                             status: Int,
                             detail: Option[String] = None,
                             traceId: Option[String] = None,
                             instance: Option[String] = None,
                             details: Option[A] = None
                           )
@Deprecated
object Problem {

  def apply[T](e: T): Problem[T] = Problem(
    "",
    "",
    500,
    None,
    None,
    None,
    Some(e)
  )

  given [A](using JsonValueCodec[A]): JsonValueCodec[Problem[A]] =
    JsonCodecMaker.make

  given [A: Schema]: Schema[Problem[A]] =
    Schema.derived
}
