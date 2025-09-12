package aephyr.kernel.jsoniter

import scala.language.unsafeNulls

import aephyr.kernel.types.Base64Url
import com.github.plokhotnyuk.jsoniter_scala.core.*

given base64UrlJsonValueCodec: JsonValueCodec[Base64Url] =
  new JsonValueCodec[Base64Url] {
    // used only when reading JSON null without a default; choose a harmless value
    override def nullValue: Base64Url = Base64Url.unsafe("")

    override def encodeValue(x: Base64Url, out: JsonWriter): Unit =
      out.writeVal(x.value)

    override def decodeValue(in: JsonReader, default: Base64Url): Base64Url = {
      if (in.isNextToken('n')) in.readNullOrError(default, "null unexpected for Base64Url")
      else {
        in.rollbackToken()
        val s = in.readString(null)
        Base64Url.parse(s) match
          case Right(v) => v
          case Left(err) => in.decodeError(err)
      }
    }
  }
