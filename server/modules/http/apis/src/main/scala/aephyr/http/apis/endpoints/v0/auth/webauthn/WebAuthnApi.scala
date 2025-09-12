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

//  val eBeginAuth: PublicEndpoint[BeginAuthInput, Problem[WebAuthnError], BeginAuthOutput, Any] =
//    webauthn
//      .post
//      .in("authentication" / "options")
//      .in(jsonIn[BeginAuthInput])
//      .out(jsonOut[BeginAuthOutput])

//  val eFinishAuth: PublicEndpoint[FinishAuthInput, Problem[WebAuthnError], StatusCode, Any] =
//    webauthn
//      .post
//      .in("authentication" / "verify")
//      .in(jsonIn[FinishAuthInput])
//      .out(statusCode)

//  val endpoints = List(eBeginReg, eFinishReg, eBeginAuth, eFinishAuth)
  val endpoints = List(registrationOptions)
}
