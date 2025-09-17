package aephyr.auth.ports

import aephyr.identity.domain.auth.AuthError
import aephyr.kernel.id.UserId
import zio.IO

final case class VerifiedJwt(
  userId: UserId,
  roles: Set[String],
  issuedAtEpochSec: Long,
  expiresAtEpochSec: Long,
  kid: Option[String]
)

trait JwtVerifier {
  def verifyAccess(token: String): IO[AuthError, VerifiedJwt]
}
