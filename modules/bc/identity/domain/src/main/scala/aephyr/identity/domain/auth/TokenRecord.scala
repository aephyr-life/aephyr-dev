package aephyr.identity.domain.auth

import java.time.Instant

import aephyr.identity.domain.User

final case class TokenRecord(
  hash: String,
  userId: User.Id,
  purpose: String, // z.B. "login" | "verifyEmail" | …
  expiresAt: Instant
)
