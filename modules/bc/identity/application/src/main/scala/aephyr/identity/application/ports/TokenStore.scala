package aephyr.identity.application.ports

import java.time.Instant

import aephyr.identity.domain.User
import aephyr.identity.domain.auth.{ TokenRecord, TokenStoreError }
import zio.IO

trait TokenStore:

  def put(
    hash: String,
    userId: User.Id,
    purpose: String,
    expiresAt: Instant,
    singleUse: Boolean
  ): IO[TokenStoreError, Unit]

  def consumeSingleUse(
    hash: String,
    instant: Instant
  ): IO[TokenStoreError, TokenRecord]
