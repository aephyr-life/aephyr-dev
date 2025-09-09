package aephyr.http.server.mapping.webauthn

import zio.{IO, UIO, ZIO}
import aephyr.api.shared.Problem
import aephyr.kernel.id.UserId
import aephyr.kernel.types.Base64Url.*
import aephyr.auth.application.webauthn.WebAuthnService.{BeginRegCmd, BeginRegResult}
import aephyr.auth.domain
import aephyr.http.apis.endpoints.v0.auth.webauthn.{
  WebAuthnErrorDto => ApiErr,
  BeginRegInput => DBeginRegInput,
  BeginRegOutput => DBeginRegOutput
}
import aephyr.http.apis.endpoints.v0.auth.webauthn.model.{
  Attestation => DAttestation,
  AuthenticatorSelection => DAuthSel,
  CredDescriptor => DCredDescriptor,
  PubKeyCredParam => DCredParam,
  RpEntity => DRpEntity,
  UserEntity => DUserEntity
}

object WebAuthnDtoMapper {

  // ---------- API -> Domain --------------------------------------------------

  private def toDomainAuthSel(in: DAuthSel): domain.webauthn.AuthenticatorSelection =
    // TODO: map fields 1:1 when ready
    domain.webauthn.AuthenticatorSelection(
      residentKey             = None,
      userVerification        = None,
//      requireResidentKey      = None,
      attachment = None
    )

  private def toDomainAttestation(in: DAttestation): Option[domain.webauthn.AttestationConveyance] =
    in match
      case DAttestation.none       => None
      case DAttestation.direct     => Some(domain.webauthn.AttestationConveyance.Direct)
      case DAttestation.indirect   => Some(domain.webauthn.AttestationConveyance.Indirect)
      case DAttestation.enterprise => Some(domain.webauthn.AttestationConveyance.Enterprise)

  def beginRegCmd(in: DBeginRegInput): IO[Problem[ApiErr], BeginRegCmd] =
    ZIO.fromEither(UserId.fromString(in.userId))
      .mapError(_ => Problem(ApiErr.InvalidChallenge))
      .map { uid =>
        BeginRegCmd(
          userId                 = uid,
          username               = in.username,
          displayName            = Option(in.displayName).filter(_.nonEmpty),
          authenticatorSelection = in.authenticatorSelection.map(toDomainAuthSel),
          attestation            = in.attestation.flatMap(toDomainAttestation),
          excludeCredentials     = Nil // fill when you pass descriptors
        )
      }

  // ---------- Domain -> API --------------------------------------------------

  private def rpEntity(o: domain.webauthn.CreationOptions): DRpEntity =
    DRpEntity(
      name = o.rp.name,
      id   = Option(o.rp.id).map(_.value) // if already Option, this is still fine
    )

  private def userEntity(o: domain.webauthn.CreationOptions): DUserEntity =
    DUserEntity(
      id          = o.user.handle.bytes.toBase64Url,
      name        = o.user.name,
      displayName = o.user.displayName.getOrElse("")
    )

  private def challenge(o: domain.webauthn.CreationOptions) =
    o.challenge.bytes.toBase64Url

  private def pubKeyCredParams(o: domain.webauthn.CreationOptions): List[DCredParam] =
    // TODO: build from your supported algs (e.g., ES256, EdDSA)
    Nil

  private def timeout(o: domain.webauthn.CreationOptions): Option[Long] = None

  private def excludeCreds(o: domain.webauthn.CreationOptions): Option[List[DCredDescriptor]] = None

  private def authSel(o: domain.webauthn.CreationOptions): Option[DAuthSel] = None

  private def attestation(o: domain.webauthn.CreationOptions): Option[DAttestation] = None

  def beginRegOut(res: BeginRegResult): UIO[DBeginRegOutput] = {
    val o = res.options
    ZIO.succeed(
      DBeginRegOutput(
        rp                     = rpEntity(o),
        user                   = userEntity(o),
        challenge              = challenge(o),
        pubKeyCredParams       = pubKeyCredParams(o),
        timeout                = timeout(o),
        excludeCredentials     = excludeCreds(o),
        authenticatorSelection = authSel(o),
        attestation            = attestation(o),
        extensions             = None,
        tx                     = res.tx
      )
    )
  }

  // ---------- Errors (domain -> API Problem) --------------------------------

  def toProblem(e: domain.webauthn.WebAuthnError): Problem[ApiErr] = e match
    // TODO: map concrete cases 1:1
    // case domain.webauthn.WebAuthnError.OriginNotAllowed => Problem(ApiErr.OriginNotAllowed)
    // ...
    case other => Problem(ApiErr.ServerError(other.toString))
}
