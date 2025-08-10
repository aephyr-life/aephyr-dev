package aephyr.identity.domain.auth

import aephyr.identity.domain.User

import java.time.Instant

final case class MagicTokenRecord(
                                   hash: String,
                                   userId: User.Id,
                                   purpose: String,                // z.B. "login" | "verifyEmail" | …
                                   expiresAt: Instant,
                                   usedAt: Option[Instant],
                                   createdIp: Option[String] = None,
                                   ua: Option[String] = None
                                 )
