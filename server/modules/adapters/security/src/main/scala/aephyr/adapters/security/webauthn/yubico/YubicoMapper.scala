package aephyr.adapters.security.webauthn.yubico

import aephyr.auth.domain.webauthn.UserEntity

import scala.language.unsafeNulls
import com.yubico.webauthn.data.*



object YubicoMapper {

  def toYUser(user: UserEntity): UserIdentity =
    UserIdentity.builder()
      .name(user.name)
      .displayName(user.displayName)
      .id(new ByteArray(user.id.bytesCopy))
      .build()
}
