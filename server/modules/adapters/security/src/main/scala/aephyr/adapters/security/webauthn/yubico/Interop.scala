package aephyr.adapters.security.webauthn.yubico

import aephyr.auth.domain.UserHandle
import aephyr.auth.domain.webauthn.*
import aephyr.kernel.types.Bytes
import com.yubico.webauthn.data.{ByteArray, UserIdentity}

object Interop:
  
  extension (b: Bytes)
    def toByteArray: ByteArray = new ByteArray(b.toArray)

  extension (ba: ByteArray)
    def toBytes: Bytes = 
      Bytes.fromArray(ba.getBytes.nn)
    def toCredentialId: CredentialId =
      CredentialId.unsafe(Bytes.fromArray(ba.getBytes.nn))
      
    def toPublicKey: PublicKeyCose =
      PublicKeyCose.unsafe(Bytes.fromArray(ba.getBytes.nn))
      
    def toUserHandle: UserHandle =
      UserHandle(Bytes.fromArray(ba.getBytes.nn))
  
  extension (ue: UserEntity)
    def toYUser: UserIdentity =
        UserIdentity.builder().nn
          .name(ue.name).nn
          .displayName(ue.displayName).nn
          .id(new ByteArray(ue.id.bytesCopy)).nn
          .build().nn
