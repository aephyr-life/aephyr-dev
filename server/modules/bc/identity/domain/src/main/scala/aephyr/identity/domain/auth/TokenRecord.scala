package aephyr.identity.domain.auth

import aephyr.kernel.id.UserId

import java.time.Instant

final case class TokenRecord(
  hash: String,
  userId: UserId,
  purpose: String, // z.B. "login" | "verifyEmail" | …
  expiresAt: Instant
)
