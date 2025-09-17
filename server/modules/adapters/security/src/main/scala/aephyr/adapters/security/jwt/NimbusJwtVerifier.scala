package aephyr.adapters.security.jwt

import scala.language.unsafeNulls
import aephyr.auth.ports.{JwtVerifier, VerifiedJwt}
import aephyr.identity.domain.auth.AuthError
import aephyr.kernel.id.UserId
import zio.*
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jose.*
import com.nimbusds.jose.jwk.*
import com.nimbusds.jose.crypto.{Ed25519Verifier, MACVerifier}

import java.security.MessageDigest
import scala.jdk.CollectionConverters.*
import java.time.Instant

object NimbusJwtVerifier {
  /** For HS256 you can provide either a JWKSet that contains an "oct" key, or a raw sharedSecret.
   * For EdDSA provide an OKP (Ed25519) public JWK in the JWKSet.
   */
  final case class Config(
                           issuer: String,
                           audience: String,
                           jwkSetJson: String,
                           sharedSecret: String
                         )

  val live: ZLayer[Config & Clock, Nothing, JwtVerifier] =
    ZLayer.fromZIO {
      for {
        cfg   <- ZIO.service[Config]
        clock <- ZIO.clock
        jwks  <- ZIO.attempt(JWKSet.parse(cfg.jwkSetJson)).orDie
      } yield new JwtVerifier {
        def verifyAccess(token: String) = for {
          jwt   <- ZIO.attempt(SignedJWT.parse(token)).mapError(_ =>
            AuthError.InvalidToken)
          keyId  = Option(jwt.getHeader.getKeyID)
          jwkOpt = keyId.flatMap(id => Option(jwks.getKeyByKeyId(id))).orElse(jwks.getKeys.asScala.headOption)
          ok    <- verifyByAlg(jwt, jwkOpt, cfg).mapError(t =>
            t.printStackTrace()
            AuthError.InvalidToken)
          _     <- if (ok) ZIO.unit else ZIO.fail({
            AuthError.InvalidToken})
          now   <- clock.instant
          cs     = jwt.getJWTClaimsSet
          _     <- validate(cs, cfg, now)
          uid   <- ZIO
            .fromOption(Option(cs.getSubject))
            .mapError(_ =>
              AuthError.InvalidToken)
            .flatMap(s => ZIO.fromEither(UserId.fromString(s)).mapError(_ =>
              AuthError.InvalidToken))
          roles  = Option(cs.getStringListClaim("roles")).map(_.asScala.toSet)
            .orElse(Option(cs.getStringListClaim("role")).map(_.asScala.toSet))
            .getOrElse(Set.empty[String])
          iat    = Option(cs.getIssueTime).map(_.toInstant.getEpochSecond).getOrElse(0L)
          exp    = Option(cs.getExpirationTime).map(_.toInstant.getEpochSecond).getOrElse(0L)
          kidOut = keyId
        } yield VerifiedJwt(uid, roles, iat, exp, kidOut)

        private def sha256Hex(bytes: Array[Byte]): String = {
          val d = MessageDigest.getInstance("SHA-256").digest(bytes)
          d.map("%02x".format(_)).mkString
        }

        private def verifyByAlg(
                                 jwt: SignedJWT,
                                 jwkOpt: Option[JWK],
                                 cfg: Config
                               ): Task[Boolean] = ZIO.attempt {
          val algOpt = Option(jwt.getHeader.getAlgorithm) // JWSAlgorithm | Null  -> Option[JWSAlgorithm]


          algOpt match {
            case Some(a) if a.eq(JWSAlgorithm.HS256) || a.eq(JWSAlgorithm.HS384) || a.eq(JWSAlgorithm.HS512) =>
//              val macKeyBytes: Array[Byte] =
//                jwkOpt.collect { case k: OctetSequenceKey => k.toByteArray }
//                  .orElse(cfg.sharedSecret.getBytes("UTF-8"))
//                  .getOrElse(Array.emptyByteArray)
              val macKeyBytes = cfg.sharedSecret.getBytes("UTF-8")  // 👈 dev-only hardcoded secret
              require(macKeyBytes.nonEmpty, "No HMAC secret provided (oct JWK or sharedSecret)")
              jwt.verify(new MACVerifier(macKeyBytes))

            case Some(a) if a.eq(JWSAlgorithm.EdDSA) =>
              val okp =
                jwkOpt.collect {
                  case k: OctetKeyPair if Option(k.getCurve).contains(Curve.Ed25519) => k
                }.getOrElse(throw new IllegalStateException("Missing Ed25519 OKP JWK for EdDSA token"))

              jwt.verify(new Ed25519Verifier(okp))

            case Some(other) =>
              throw new IllegalArgumentException(s"Unsupported JWS alg: $other")

            case None =>
              throw new IllegalStateException("JWT header 'alg' is null")
          }
        }

        private def validate(cs: com.nimbusds.jwt.JWTClaimsSet, cfg: Config, now: Instant) = {
          val hasIss = Option(cs.getIssuer).exists(_ == cfg.issuer)
          val hasAud = Option(cs.getAudience).exists(_.asScala.contains(cfg.audience))
          val expOk  = Option(cs.getExpirationTime).exists(_.toInstant.isAfter(now.minusSeconds(30)))
          val nbfOk  = Option(cs.getNotBeforeTime).forall(_.toInstant.isBefore(now.plusSeconds(30)))

          // ⚠️ If you don’t put iss/aud in your tokens yet, relax them temporarily:
          val issOk = if (cfg.issuer.isEmpty) true else hasIss
          val audOk = if (cfg.audience.isEmpty) true else hasAud

          if (issOk && audOk && expOk && nbfOk) ZIO.unit
          else if (!expOk) ZIO.fail(AuthError.TokenExpired)
          else ZIO.fail(AuthError.InvalidToken)
        }
      }
    }
}
