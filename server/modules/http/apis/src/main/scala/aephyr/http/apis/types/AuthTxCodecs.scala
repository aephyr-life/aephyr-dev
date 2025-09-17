package aephyr.http.apis.types

import aephyr.auth.domain.AuthTx
import sttp.tapir.{Codec, DecodeResult, Schema}
import sttp.tapir.Codec.PlainCodec
import com.github.plokhotnyuk.jsoniter_scala.core.*

object AuthTxCodecs {

  given JsonValueCodec[AuthTx] = new JsonValueCodec[AuthTx] {
    def decodeValue(in: JsonReader, default: AuthTx): AuthTx =
      AuthTx(in.readString(null.asInstanceOf[String]))

    def encodeValue(x: AuthTx, out: JsonWriter): Unit = out.writeVal(x.value)

    def nullValue: AuthTx = AuthTx("")
  }

  // OpenAPI schema
  given Schema[AuthTx] =
    Schema.schemaForString
      .map[AuthTx](s => Some(AuthTx(s)))(_.value)
      .description("Opaque auth transaction token")

  // Tapir path/query codec
  given PlainCodec[AuthTx] =
    Codec.string.mapDecode { s =>
      aephyr.auth.domain.AuthTx.parse(s) match
        case Right(tx) => DecodeResult.Value(tx)
        case Left(err) => DecodeResult.Error(s, new Exception(err))
    }(_.value)
}
