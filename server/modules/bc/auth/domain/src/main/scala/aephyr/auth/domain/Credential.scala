package aephyr.auth.domain

import java.time.Instant
import java.util.UUID
import aephyr.kernel.id.UserId
import aephyr.kernel.types.Bytes

/** WebAuthn credential persisted in auth.user_credential. */
final case class Credential(
                             id: UUID,
                             userId: UserId,
                             credentialId: Bytes,   // raw credentialId (opaque)
                             publicKeyCose: Bytes,  // COSE-encoded public key
                             signCount: Long,
                             userHandleBytes: Bytes,
                             uvRequired: Boolean,
                             transports: List[String],
                             label: Option[String],
                             createdAt: Instant,
                             updatedAt: Instant,
                             lastUsedAt: Option[Instant]
                           )
