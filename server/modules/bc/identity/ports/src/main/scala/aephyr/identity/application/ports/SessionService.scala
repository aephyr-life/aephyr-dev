package aephyr.identity.application.ports

import java.time.Instant

import zio.*

import aephyr.kernel.id.UserId

final case class SessionId(value: String) extends AnyVal

final case class Session(
                          id: SessionId,
                          userId: UserId,
                          createdAt: Instant,
                          lastSeenAt: Instant,
                          expiresAt: Instant
                        )

trait SessionService {
  def create(userId: UserId): UIO[Session]
  def get(id: SessionId): UIO[Option[Session]]
  def touch(id: SessionId): UIO[Unit]
  def revoke(id: SessionId): UIO[Unit]
}
