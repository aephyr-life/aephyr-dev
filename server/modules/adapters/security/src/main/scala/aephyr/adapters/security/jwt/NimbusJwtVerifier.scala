package aephyr.adapters.security.jwt

import scala.language.unsafeNulls // TODO clean up and remove unsafeNulls

import aephyr.auth.ports.{JwtVerifier, VerifiedJwt}
import aephyr.identity.domain.auth.AuthError
import aephyr.kernel.id.UserId
import zio.*
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.crypto.Ed25519Verifier

import scala.jdk.CollectionConverters.*
import java.time.Instant

object NimbusJwtVerifier {
  final case class Config(issuer: String, audience: String, jwkSetJson: String)

  val live: ZLayer[Config & Clock, Nothing, JwtVerifier] =
    ZLayer.fromZIO {
      for {
        cfg    <- ZIO.service[Config]
        clock  <- ZIO.clock
        jwks   <- ZIO.attempt(JWKSet.parse(cfg.jwkSetJson)).orDie
      } yield new JwtVerifier {
        def verifyAccess(token: String) = for {
          jwt <- ZIO.attempt(SignedJWT.parse(token)).mapError(_ => AuthError.InvalidToken)
          jwk  = Option(jwt.getHeader.getKeyID)
            .flatMap(k => Option(jwks.getKeyByKeyId(k)))
            .orElse(jwks.getKeys.asScala.headOption)
          pub <- ZIO.fromOption(jwk).mapError(_ => AuthError.InvalidToken)
          octetKeyPair = pub.toPublicJWK.asInstanceOf[com.nimbusds.jose.jwk.OctetKeyPair]
          ok  <- ZIO.succeed(jwt.verify(new Ed25519Verifier(octetKeyPair)))
          _   <- if (ok) ZIO.unit else ZIO.fail(AuthError.InvalidToken)
          now <- clock.instant
          cs   = jwt.getJWTClaimsSet
          _   <- validate(cs, cfg, now)
          uid <- ZIO
            .fromOption(Option(cs.getSubject))
            .mapError(_ => AuthError.InvalidToken)
            .flatMap(s => ZIO.fromEither(UserId.fromString(s)).mapError(_ => AuthError.InvalidToken))
          roles = Option(cs.getStringListClaim("roles")).map(_.asScala.toSet)
            .orElse(Option(cs.getStringListClaim("role")).map(_.asScala.toSet))
            .getOrElse(Set.empty[String])
          iat  = Option(cs.getIssueTime).map(_.toInstant.getEpochSecond).getOrElse(0L)
          exp  = Option(cs.getExpirationTime).map(_.toInstant.getEpochSecond).getOrElse(0L)
          kid  = Option(jwt.getHeader.getKeyID)
        } yield VerifiedJwt(uid, roles, iat, exp, kid)

        private def validate(cs: com.nimbusds.jwt.JWTClaimsSet, cfg: Config, now: Instant) = {
          val issOk = Option(cs.getIssuer).contains(cfg.issuer)
          val audOk = Option(cs.getAudience).exists(_.asScala.contains(cfg.audience))
          val expOk = Option(cs.getExpirationTime).exists(_.toInstant.isAfter(now.minusSeconds(30)))
          val nbfOk = Option(cs.getNotBeforeTime).forall(_.toInstant.isBefore(now.plusSeconds(30)))
          if (issOk && audOk && expOk && nbfOk) ZIO.unit
          else if (!expOk) ZIO.fail(AuthError.TokenExpired)
          else ZIO.fail(AuthError.InvalidToken)
        }
      }
    }
}
