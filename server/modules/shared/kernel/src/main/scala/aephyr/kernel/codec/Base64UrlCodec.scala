package aephyr.kernel.codec

import java.util.Base64
import scala.util.Try

object Base64UrlCodec {
  private val encoder = Base64.getUrlEncoder.nn.withoutPadding().nn
  private val decoder = Base64.getUrlDecoder.nn

  /** Encode bytes into Base64Url (no padding). */
  def encode(bytes: Array[Byte]): String =
    encoder.encodeToString(bytes).nn

  /** Decode Base64Url string into bytes. */
  def decode(str: String): Either[Throwable, Array[Byte]] =
    Try(decoder.decode(str).nn).toEither

  /** Convenience: encode UTF-8 string into Base64Url. */
  def encodeString(str: String): String =
    encode(str.getBytes("UTF-8").nn)

  /** Convenience: decode Base64Url into UTF-8 string. */
  def decodeString(str: String): Either[Throwable, String] =
    decode(str).map(bytes => new String(bytes, "UTF-8"))
}
