package aephyr.http.apis.types

import com.github.plokhotnyuk.jsoniter_scala.core._
import sttp.tapir.Schema

final case class RawJson(value: String) extends AnyVal

object RawJson {
  given JsonValueCodec[RawJson] = new JsonValueCodec[RawJson] {
    def decodeValue(in: JsonReader, default: RawJson): RawJson =
      RawJson(new String(in.readRawValAsBytes(), java.nio.charset.StandardCharsets.UTF_8))
    def encodeValue(x: RawJson, out: JsonWriter): Unit =
      out.writeRawVal(x.value.getBytes.nn) // <-- embed without quotes
    def nullValue: RawJson = RawJson("null")
  }
  given Schema[RawJson] = Schema.any
}
