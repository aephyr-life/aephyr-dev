package aephyr.kernel.types

import aephyr.kernel.codec.Base64UrlCodec

opaque type Base64Url = String

object Base64Url:
  // ---- Construction ---------------------------------------------------------
  /** Validate string is base64url (no padding) and decodable. */
  def parse(s: String): Either[String, Base64Url] =
    val trimmed = s.trim.nn
    val okChars = trimmed.matches("^[A-Za-z0-9_-]+$") // no '=' padding
    if !okChars then Left("Not base64url (allowed: A-Z a-z 0-9 _ -)")
    else
      Base64UrlCodec.decode(trimmed).left.map(_.getMessage.nn).map(_ => trimmed)

  /** Unsafe constructor (assumes already valid base64url). */
  def unsafe(s: String): Base64Url =
    parse(s).fold(err => throw new IllegalArgumentException(err), identity)

  /** Encode bytes -> base64url. */
  def fromBytes(bytes: Bytes): Base64Url =
    unsafe(Base64UrlCodec.encode(bytes.toArray))

  def fromArray(a: Array[Byte]): Base64Url =
    unsafe(Base64UrlCodec.encode(a))

  // ---- Decoding -------------------------------------------------------------
  extension (b64: Base64Url)
    def value: String = b64

    def toArrayEither: Either[String, Array[Byte]] =
      Base64UrlCodec.decode(b64).left.map(_.getMessage.nn)

    def toBytesEither: Either[String, Bytes] =
      toArrayEither.map(Bytes.fromArray)

  extension (b: Bytes)
    def toBase64Url: Base64Url = fromBytes(b)

  extension (a: Array[Byte])
    def toBase64Url: Base64Url = fromArray(a)
