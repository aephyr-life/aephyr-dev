package aephyr.auth.domain.webauthn

import aephyr.kernel.types.Bytes

opaque type CredentialId = Bytes

object CredentialId:
  
  def apply(b: Bytes): Either[String, CredentialId] =
    if b.isEmpty then Left("credentialId must be non-empty") else Right(b)
  
  def unsafe(b: Bytes): CredentialId = b
  
  extension (id: CredentialId)
    inline def bytes: Bytes = id
