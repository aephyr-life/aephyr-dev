package aephyr.adapters.security.jwt

import aephyr.auth.ports.JwtIssuer
import aephyr.kernel.id.UserId
import zio.{UIO, ULayer, ZIO, ZLayer}
import com.nimbusds.jose.{JWSAlgorithm, JWSHeader}
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.{JWTClaimsSet, SignedJWT}

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Date
import scala.jdk.CollectionConverters.*

object NimbusJwtIssuer extends JwtIssuer {

  // Configure as you like (env or hardcoded default)
  private val DefaultTtlSeconds: Long = 3600L
  private val Secret: Array[Byte] =
    sys.env.get("JWT_HS256_SECRET").nn
      .getOrElse("this_is_at_least_32_characters_long_bla_blub").nn // ⚠️ replace in prod
      .getBytes(StandardCharsets.UTF_8).nn

  /** @return (token, ttlSeconds) */
  override def mintAccess(userId: UserId, roles: Set[String]): UIO[(String, Long)] =
    ZIO.succeed {
      val now        = new Date()
      val ttlSeconds = DefaultTtlSeconds
      val exp        = new Date(now.getTime + ttlSeconds * 1000)

      val claims = new JWTClaimsSet.Builder()
        .subject(userId.value.toString).nn
        .issueTime(now).nn
        .expirationTime(exp).nn
        .claim("roles", roles.toList.asJava).nn
        .build().nn

      val header   = new JWSHeader(JWSAlgorithm.HS256)
      val signed   = new SignedJWT(header, claims)
      val signer   = new MACSigner(Secret)

      signed.sign(signer)
      val token = signed.serialize().nn
      
      (token, ttlSeconds)
    }

  private def sha256Hex(bytes: Array[Byte]): String = {
    val d = MessageDigest.getInstance("SHA-256").nn.digest(bytes).nn
    d.map("%02x".format(_)).mkString
  }

  val layer: ULayer[JwtIssuer] =
    ZLayer.succeed(NimbusJwtIssuer)
}
