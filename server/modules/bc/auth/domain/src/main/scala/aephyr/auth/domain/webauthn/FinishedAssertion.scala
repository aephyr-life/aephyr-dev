package aephyr.auth.domain.webauthn

import aephyr.kernel.types.Bytes

final case class FinishedAssertion(
  credentialId: Bytes,
  userHandle: Option[Bytes],
  signCount: Long,
  userVerified: Boolean
)
