package aephyr.adapters.security.session

import aephyr.identity.application.ports.{
  Session,
  SessionId,
  SessionService,
  UserId
}
import aephyr.shared.config.MagicLinkCfg
import aephyr.shared.security.SecureRandom
import zio._
import java.time.Instant

final class InMemorySessionService(
                                    ref: Ref[Map[SessionId, Session]],
                                    cfg: MagicLinkCfg,
                                    rnd: SecureRandom,
                                    clock: Clock
                                  ) extends SessionService {

  private val ttlSec: Long = cfg.cookie.maxAgeSec.getOrElse(12 * 60 * 60).toLong

  def create(userId: UserId): UIO[Session] =
    for {
      now <- clock.instant
      raw <- rnd.nextBytesAsBase64(32)
      id   = SessionId(raw)
      s    = Session(id, userId, now, now, now.plusSeconds(ttlSec).nn)
      _   <- ref.update(_ + (id -> s))
    } yield s

  def get(id: SessionId): UIO[Option[Session]] =
    for {
      now <- clock.instant
      s   <- ref.get.map(_.get(id).filter(_.expiresAt.isAfter(now)))
    } yield s

  def touch(id: SessionId): UIO[Unit] =
    for {
      now <- clock.instant
      _   <- ref.update { m =>
        m.get(id).fold(m)(s =>
          m.updated(id, s.copy(lastSeenAt = now, expiresAt = now.plusSeconds(ttlSec).nn)))
      }
    } yield ()

  def revoke(id: SessionId): UIO[Unit] =
    ref.update(_ - id).unit
}

object InMemorySessionService {
  val layer: ZLayer[MagicLinkCfg & SecureRandom & Clock, Nothing, SessionService] =
    ZLayer.fromZIO {
      for {
        ref <- Ref.make(Map.empty[SessionId, Session])
        cfg <- ZIO.service[MagicLinkCfg]
        rnd <- ZIO.service[SecureRandom]
        clk <- ZIO.service[Clock]
        svc  = new InMemorySessionService(ref, cfg, rnd, clk)
        _   <- (for {
          now <- clk.instant
          _   <- ref.update(_.filter { case (_, s) => s.expiresAt.isAfter(now) })
          _   <- ZIO.sleep(5.minutes)
        } yield ()).forever.forkDaemon
      } yield svc
    }
}

