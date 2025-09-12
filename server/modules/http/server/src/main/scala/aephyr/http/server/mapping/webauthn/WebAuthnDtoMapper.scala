package aephyr.http.server.mapping.webauthn

import zio.{IO, ZIO}
import aephyr.api.shared.Problem
import aephyr.kernel.id.UserId
import aephyr.auth.application.webauthn.WebAuthnService.BeginRegCmd
import aephyr.auth.domain
import aephyr.http.apis.endpoints.v0.auth.webauthn.{
  WebAuthnErrorDto => ApiErr,
  BeginRegInput => DBeginRegInput
}


object WebAuthnDtoMapper {

  // ---------- API -> Domain --------------------------------------------------
  
  def beginRegCmd(in: DBeginRegInput): IO[Problem[ApiErr], BeginRegCmd] =
    ZIO.fromEither(UserId.fromString(in.userId))
      .mapError(_ => Problem(ApiErr.InvalidChallenge))
      .map { uid =>
        BeginRegCmd(
          userId      = uid,
          username    = in.username,
          displayName = in.displayName
        )
      }

  // ---------- Domain -> API --------------------------------------------------


  // ---------- Errors (domain -> API Problem) --------------------------------

  def toProblem(e: domain.webauthn.WebAuthnError): Problem[ApiErr] = e match
    // TODO: map concrete cases 1:1
    // case domain.webauthn.WebAuthnError.OriginNotAllowed => Problem(ApiErr.OriginNotAllowed)
    // ...
    case other => Problem(ApiErr.ServerError(other.toString))
}
