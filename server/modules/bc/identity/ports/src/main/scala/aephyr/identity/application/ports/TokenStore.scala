package aephyr.identity.application.ports

import java.time.Instant

import aephyr.kernel.id.UserId

import aephyr.identity.domain.auth.{ TokenRecord, TokenStoreError }
import zio.IO

trait TokenStore:

  def put(
    hash: String,
    userId: UserId,
    purpose: String,
    expiresAt: Instant,
    singleUse: Boolean
  ): IO[TokenStoreError, Unit]

  def consumeSingleUse(
    hash: String,
    instant: Instant
  ): IO[TokenStoreError, TokenRecord]
