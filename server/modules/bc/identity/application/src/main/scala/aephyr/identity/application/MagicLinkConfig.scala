package aephyr.identity.application

import javax.crypto.spec.SecretKeySpec

import zio._

final case class MagicLinkConfig(
  baseUrl: String,
  ttl: Duration = 15.minutes,
  hmacKey: SecretKeySpec
)

//object MagicLinkConfig:
//
//  private def decodeBase64UrlNoPadOrThrow(raw: String): Array[Byte] = {
//    val t = raw.trim.replaceAll("\\s", "")
//    val pad = (4 - (t.length % 4)) % 4
//    val bytes =
//      try Base64.getUrlDecoder.decode(t + ("=" * pad))
//      catch {
//        case e: IllegalArgumentException =>
//          throw new IllegalArgumentException(s"Invalid Base64URL secret: ${e.getMessage}")
//      }
//    if (bytes.length < 32)
//      throw new IllegalArgumentException(s"Secret too short: ${bytes.length} bytes (need >= 32)")
//    bytes
//  }
//
//  val secretKeyConfig: Config[SecretKeySpec] =
//    Config.string("HMAC_SECRET_B64URL").mapAttempt { raw =>
//      val bytes = decodeBase64UrlNoPadOrThrow(raw)
//      new SecretKeySpec(bytes, "HmacSHA256")
//    }
//
//  val magicLinkConfig: Config[MagicLinkConfig] =
//    (zio.Config.string("BASE_URL") ++
//      zio.Config.duration("TTL") ++
//      secretKeyConfig
//      ).map { case (baseUrl, ttl, key) =>
//      MagicLinkConfig(baseUrl, ttl, key)
//    }
//
//  val layer: ZLayer[Any, Config.Error, MagicLinkConfig] =
//    ZLayer.fromZIO(ZIO.config(magicLinkConfig))
