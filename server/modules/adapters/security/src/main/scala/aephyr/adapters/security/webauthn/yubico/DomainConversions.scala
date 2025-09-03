package aephyr.adapters.security.webauthn.yubico

import scala.language.unsafeNulls

import aephyr.auth.domain.webauthn.AttestationConveyance.{Direct, Enterprise, Indirect}
import aephyr.auth.domain.webauthn.{AttestationConveyance, AuthenticatorSelection, CredDescriptor, ResidentKey, UserEntity, UserVerification}
import com.yubico.webauthn.data.{AttestationConveyancePreference, AuthenticatorSelectionCriteria, ByteArray, PublicKeyCredentialDescriptor, ResidentKeyRequirement, UserIdentity, UserVerificationRequirement}

import java.util
import scala.jdk.CollectionConverters.*


private[yubico] object DomainConversions {

  // Minimal domain wrappers I assume you have:
  //   case class CreationOptions(json: String)
  //   case class RequestOptions(json: String)
  //   case class CredentialId(bytes: Array[Byte])
  //   case class PublicKeyCose(bytes: Array[Byte])
  //   case class UserEntity(name: String, displayName: String, id: UserHandle)
  //   case class CredDescriptor(idB64Url: String)
  //   enum UserVerification { case Discouraged, Preferred, Required }

  inline def unwrapPublicKey(json: String): String =
    // Yubico returns: {"publicKey":{...}} for *.toCredentials*Json.
    // Fast-path: if it starts with {"publicKey":
    val trimmed = json.dropWhile(_.isWhitespace)
    if trimmed.startsWith("""{"publicKey":""") then
      // naive but safe enough for this exact shape: strip the outer {"publicKey": ...}
      // We find the first '{' after the colon and then match closing '}'.
      // Since Yubico controls the format, this is stable.
      val idxFirstBrace = trimmed.indexOf('{', /*from*/ trimmed.indexOf(':') + 1)
      // count braces to find the matching one
      var depth = 0
      var i = idxFirstBrace
      var end = -1
      while i < trimmed.length do
        trimmed.charAt(i) match
          case '{' => depth += 1
          case '}' =>
            depth -= 1
            if depth == 0 && end < 0 then end = i
          case _ => ()
        i += 1
      if idxFirstBrace >= 0 && end > idxFirstBrace then trimmed.substring(idxFirstBrace, end + 1).nn
      else trimmed // fallback
    else json

  def domAttestation(a: Option[AttestationConveyance]): AttestationConveyancePreference =
    a match {
      case None => AttestationConveyancePreference.NONE
      case Some(Indirect) => AttestationConveyancePreference.INDIRECT
      case Some(Direct) => AttestationConveyancePreference.DIRECT
      case Some(Enterprise) => AttestationConveyancePreference.ENTERPRISE
    }

  def domUserVerification(u: UserVerification): UserVerificationRequirement =
    u match
      case UserVerification.Discouraged => UserVerificationRequirement.DISCOURAGED
      case UserVerification.Preferred => UserVerificationRequirement.PREFERRED
      case UserVerification.Required => UserVerificationRequirement.REQUIRED

  def toYubicoUser(u: UserEntity): UserIdentity =
    UserIdentity
      .builder()
      .name(u.name)
      .displayName(u.displayName.orNull)
      .id(new ByteArray(u.handle.bytes.toArray)) // TODO handle the same as ID?
      .build()

  def toYubicoSelection(s: AuthenticatorSelection): AuthenticatorSelectionCriteria =
    val b = AuthenticatorSelectionCriteria.builder().nn
    s.residentKey.foreach {
      case ResidentKey.Discouraged => b.residentKey(ResidentKeyRequirement.DISCOURAGED)
      case ResidentKey.Preferred => b.residentKey(ResidentKeyRequirement.PREFERRED)
      case ResidentKey.Required => b.residentKey(ResidentKeyRequirement.REQUIRED)
    }
    s.userVerification.foreach(u => b.userVerification(domUserVerification(u)))
    b.build()

  def toYubicoExclude(list: List[CredDescriptor]): util.List[PublicKeyCredentialDescriptor] =
    list.map { cd =>
      // Assuming CredDescriptor holds a Base64Url string of the credentialId
      //val idBytes = aephyr.kernel.codec.Base64UrlCodec.decode(cd.id.bytes)
      PublicKeyCredentialDescriptor.builder()
        //.`type`(PublicKeyCredentialType.PUBLIC_KEY) // TODO do I need to set the type?
        .id(new ByteArray(cd.id.bytes.toArray))
        // TODO should the id be base64?
        .build()
    }.asJava
}
