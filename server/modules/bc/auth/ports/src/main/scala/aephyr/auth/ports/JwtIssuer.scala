package aephyr.auth.ports

import aephyr.kernel.id.UserId
import zio.{UIO, ZIO}

trait JwtIssuer {
  /** @return (token, ttlSeconds) */
  def mintAccess(userId: UserId, roles: Set[String] = Set.empty): UIO[(String, Long)]
}

object JwtIssuer {
  def mintAccess(userId: UserId, roles: Set[String] = Set.empty)
  : ZIO[JwtIssuer, Nothing, (String, Long)] =
    ZIO.serviceWithZIO[JwtIssuer](_.mintAccess(userId, roles))
}
