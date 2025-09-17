package aephyr.auth.domain.webauthn

import aephyr.auth.domain.UserHandle

final case class UserEntity(
  id: UserHandle,
  name: String,
  displayName: String
) derives CanEqual
