package aephyr.identity.application.ports

import aephyr.identity.domain.User
import aephyr.identity.domain.auth.MagicTokenRecord
import aephyr.identity.domain.auth.AuthError
import aephyr.identity.domain.auth.TokenError
import zio.{IO, UIO, ZIO}

import java.time.Instant

trait TokenStore:

  def put(hash: String, userId: User.Id, purpose: String, expiresAt: Instant, singleUse: Boolean): UIO[Unit]

  def consume(hash: String, purpose: String): UIO[Option[(String, Map[String,String])]]

  def consumeSingleUse(hash: String, instant: Instant): IO[TokenError, MagicTokenRecord]

//object TokenStore:
//  def put(h: String, u: String, p: String, at: Instant, su: Boolean): ZIO[TokenStore, Nothing, Unit] =
//    ZIO.serviceWithZIO[TokenStore](_.put(h, u, p, at, su))
//
//  def consume(h: String, p: String): ZIO[TokenStore, Nothing, Option[(String, Map[String, String])]] =
//    ZIO.serviceWithZIO[TokenStore](_.consume(h, p))
