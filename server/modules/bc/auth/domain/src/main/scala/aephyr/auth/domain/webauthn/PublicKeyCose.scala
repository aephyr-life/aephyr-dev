package aephyr.auth.domain.webauthn

import aephyr.kernel.types.Bytes

opaque type PublicKeyCose = Bytes

object PublicKeyCose:

  def apply(b: Bytes): Either[String, PublicKeyCose] =
    if b.isEmpty then Left("publicKeyCose must be non-empty") else Right(b)

  def unsafe(b: Bytes): PublicKeyCose = b

  extension (p: PublicKeyCose)
    inline def bytes: Bytes = p
