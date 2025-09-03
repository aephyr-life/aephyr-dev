package aephyr.http.apis.endpoints.v0.me

import aephyr.kernel.PersistenceError
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}
import sttp.tapir.Schema

sealed trait MeError
object MeError {

  case object NotAuthenticated extends MeError
  case object UserNotFound     extends MeError
  final case class RepoFailure(msg: String) extends MeError

  def fromPersistence(e: PersistenceError): MeError =
    RepoFailure(e.toString) // TODO better error message

  given JsonValueCodec[MeError] =
    JsonCodecMaker
      .make(
        CodecMakerConfig
          .withDiscriminatorFieldName(Some("type"))
      )

  given Schema[MeError] = Schema.derived
}
