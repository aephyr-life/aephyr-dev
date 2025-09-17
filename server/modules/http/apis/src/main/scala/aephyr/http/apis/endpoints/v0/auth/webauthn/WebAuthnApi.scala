package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.api.shared.Problem
import aephyr.api.TapirDsl.*
import aephyr.auth.domain.AuthTx
import aephyr.http.apis.types.RawJson
import aephyr.http.apis.types.AuthTxCodecs.given
import sttp.model.StatusCode
import sttp.tapir.PublicEndpoint

object WebAuthnApi {

  private val webauthn = base[WebAuthnErrorDto].in("webauthn")

  val registrationOptions: PublicEndpoint[Unit, Problem[WebAuthnErrorDto], BeginRegOutput, Any] =
    webauthn
      .post
      .in("registration" / "options")
      .out(jsonOut[BeginRegOutput])

  val registrationVerify: PublicEndpoint[(AuthTx, RawJson), Problem[WebAuthnErrorDto], JwtOut, Any] =
    webauthn
      .post
      .in("registration" / path[AuthTx] / "verify")
      .in(jsonIn[RawJson])
      .out(statusCode(StatusCode.Created))
      .out(jsonOut[JwtOut])

  val authenticationOptions: PublicEndpoint[Unit, Problem[WebAuthnErrorDto], BeginAuthOutput, Any] =
    webauthn
      .post
      .in("authentication" / "options")
      .out(jsonOut[BeginAuthOutput])

  val authenticationVerify: PublicEndpoint[(AuthTx, RawJson), Problem[WebAuthnErrorDto], JwtOut, Any] =
    webauthn
      .post
      .in("authentication" / path[AuthTx] / "verify")
      .in(jsonIn[RawJson]) // the client’s PublicKeyCredential (assertion) as raw JSON
      .out(statusCode(StatusCode.Ok))
      .out(jsonOut[JwtOut])
}
