package aephyr.http.apis.types

import scala.language.unsafeNulls

import aephyr.kernel.id.UserId

import java.util.UUID

import com.github.plokhotnyuk.jsoniter_scala.core.*
import sttp.tapir.Schema

object UserIdCodecs {

  // import aephyr.kernel.id.UserId.{apply as mkUserId, fromString, value as asUuid}


  // -------- Jsoniter codec (string <-> UUID) --------
  given JsonValueCodec[UserId] = new JsonValueCodec[UserId]:
    override def nullValue: UserId =
      UserId.apply(new UUID(0L, 0L))

    override def encodeValue(x: UserId, out: JsonWriter): Unit =
      out.writeVal(x.value.toString)

    override def decodeValue(in: JsonReader, default: UserId): UserId =
      val s = in.readString(null)
      UserId.fromString(s) match
        case Right(id) => id
        case Left(err) => in.decodeError(err)

  // -------- Tapir schema (string with uuid format) --------
  given Schema[UserId] =
    Schema.schemaForUUID.map[UserId] {
      x => Some(UserId.apply(x))
    }(UserId.value)
}
