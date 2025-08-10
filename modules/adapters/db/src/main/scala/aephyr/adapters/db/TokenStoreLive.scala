package aephyr.adapters.db

import aephyr.identity.application.ports.TokenStore
import aephyr.identity.domain.User
import aephyr.identity.domain.auth.TokenError.InvalidOrExpired
import aephyr.identity.domain.auth.{MagicTokenRecord, TokenError}
import zio.{IO, UIO, ZIO, ZLayer}

import java.time.Instant

final case class TokenStoreLive() extends TokenStore:

  def put(h: String, u: User.Id, p: String, at: Instant, su: Boolean): UIO[Unit] =
    ZIO.unit

  def consume(h: String, p: String): ZIO[Any, Nothing, None.type] =
    ZIO.succeed(None)

  override def consumeSingleUse(hash: String, instant: Instant): IO[TokenError, MagicTokenRecord] =
    ZIO.fail(InvalidOrExpired)


object TokenStoreLive:

  val layer: ZLayer[Any, Nothing, TokenStore] =
    ZLayer.succeed(new TokenStoreLive)
