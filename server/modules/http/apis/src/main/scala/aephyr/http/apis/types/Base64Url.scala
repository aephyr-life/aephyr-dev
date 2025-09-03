package aephyr.http.apis.types

import scala.language.unsafeNulls

import com.github.plokhotnyuk.jsoniter_scala.core.*
import sttp.tapir.{Schema, Validator}

/** URL-safe base64 without padding, i.e. `[A-Za-z0-9_-]*` */
opaque type Base64Url = String

object Base64Url {
  // ----- constructors & ops
  def fromString(s: String): Either[String, Base64Url] =
    if (isValid(s)) Right(s) else Left("Invalid base64url (allowed: A-Z a-z 0-9 _ - ; no padding =)")

  inline def unsafe(s: String): Base64Url = s // use only if you've validated elsewhere

  extension (b64: Base64Url) inline def value: String = b64

  // ----- validation
  private val PatternStr = "^[A-Za-z0-9_-]*$"
  private val Pattern    = PatternStr.r
  inline def isValid(s: String): Boolean = Pattern.matches(s)

  // ----- jsoniter codec
  given JsonValueCodec[Base64Url] = new JsonValueCodec[Base64Url] {
    override def nullValue: Base64Url = unsafe("")
    override def decodeValue(in: JsonReader, default: Base64Url): Base64Url = {
      val s = in.readString(null)
      if (s == null) default
      else if (isValid(s)) unsafe(s)
      else in.decodeError(s"Invalid base64url string, expected $PatternStr")
    }
    override def encodeValue(x: Base64Url, out: JsonWriter): Unit =
      out.writeVal(x: String) // write underlying string
  }

  // ----- Tapir schema (for docs & validation hints)
  given Schema[Base64Url] =
  Schema.string
    .validate(Validator.pattern(PatternStr))
    .description("URL-safe base64 (no padding), characters [A-Za-z0-9_-]")
}
