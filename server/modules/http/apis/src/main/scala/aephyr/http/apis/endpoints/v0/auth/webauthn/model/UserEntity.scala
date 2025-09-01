package aephyr.http.apis.endpoints.v0.auth.webauthn.model

import aephyr.http.apis.types.Base64Url
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.tapir.Schema

final case class UserEntity(
  id: Base64Url, // user handle (byte sequence, base64url-encoded)
  name: String, // username
  displayName: String
)

object UserEntity {
  given JsonValueCodec[UserEntity] = JsonCodecMaker.make
  given Schema[UserEntity] = Schema.derived
}
