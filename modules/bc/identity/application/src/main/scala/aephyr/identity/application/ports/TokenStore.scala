package aephyr.identity.application.ports

import aephyr.identity.domain.User
import aephyr.identity.domain.auth.TokenRecord
import aephyr.identity.domain.auth.AuthError
import aephyr.identity.domain.auth.TokenStoreError
import zio.{IO, UIO, ZIO}

import java.time.Instant

trait TokenStore:

  def put(
           hash: String, 
           userId: User.Id, 
           purpose: String, 
           expiresAt: Instant, 
           singleUse: Boolean): IO[TokenStoreError, Unit]

  def consumeSingleUse(
                        hash: String, 
                        instant: Instant
                      ): IO[TokenStoreError, TokenRecord]

