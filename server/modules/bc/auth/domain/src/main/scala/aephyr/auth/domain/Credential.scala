package aephyr.auth.domain

import aephyr.auth.domain.webauthn.{CredentialId, Label, PublicKeyCose}

import java.time.Instant
import java.util.UUID
import aephyr.kernel.id.UserId

final case class Credential(
  id: UUID,
  userId: UserId,
  credentialId: CredentialId,
  publicKeyCose: PublicKeyCose,
  signCount: Long, // positive
  userHandle: UserHandle,
  uvRequired: Boolean,
  transports: List[String],
  label: Option[Label],
  createdAt: Instant,
  updatedAt: Instant,
  lastUsedAt: Option[Instant]
)
