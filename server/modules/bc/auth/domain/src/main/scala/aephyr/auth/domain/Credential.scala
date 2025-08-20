package aephyr.auth.domain

import java.time.Instant
import java.util.UUID
import aephyr.identity.domain.User

/** WebAuthn credential persisted in auth.user_credential. */
final case class Credential(
                             id: UUID,
                             userId: User.Id,
                             credentialId: Array[Byte],   // raw credentialId (opaque)
                             publicKeyCose: Array[Byte],  // COSE-encoded public key
                             signCount: Long,
                             uvRequired: Boolean,
                             transports: List[String],
                             label: Option[String],
                             createdAt: Instant,
                             updatedAt: Instant,
                             lastUsedAt: Option[Instant]
                           )
