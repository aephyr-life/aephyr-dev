package aephyr.http.server.mapping.webauthn

import aephyr.api.shared.Problem
import aephyr.auth.domain
import aephyr.http.apis.endpoints.v0.auth.webauthn.{
  WebAuthnErrorDto => ApiErr
}


object WebAuthnDtoMapper {

  // ---------- API -> Domain --------------------------------------------------

  // ---------- Domain -> API --------------------------------------------------


  // ---------- Errors (domain -> API Problem) --------------------------------

  def toProblem(e: domain.webauthn.WebAuthnError): Problem[ApiErr] = e match
    // TODO: map concrete cases 1:1
    // case domain.webauthn.WebAuthnError.OriginNotAllowed => Problem(ApiErr.OriginNotAllowed)
    // ...
    case other => Problem(ApiErr.ServerError(other.toString))
}
