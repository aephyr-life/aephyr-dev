package aephyr.kernel.tapir

import aephyr.kernel.types.Base64Url
import aephyr.kernel.types.Base64Url as B64
import sttp.tapir.*

given base64UrlSchema: Schema[Base64Url] =
  Schema.string
    .description("base64url (URL-safe, no padding)")
    .encodedExample("SGVsbG8") // "Hello"

given base64UrlTextCodec: Codec[String, Base64Url, CodecFormat.TextPlain] =
  Codec.string.mapDecode { s =>
    B64.parse(s) match
      case Right(v) => DecodeResult.Value(v)
      case Left(err) => DecodeResult.Error(err, new IllegalArgumentException(err))
  }(_.value)
