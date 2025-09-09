package aephyr.http.apis.endpoints.v0.auth.webauthn

import aephyr.api.shared.Problem
import aephyr.api.TapirDsl.*
import sttp.tapir.PublicEndpoint

object WebAuthnApi {

  private val webauthn = base[WebAuthnErrorDto].in("webauthn")

  val registrationOptions: PublicEndpoint[BeginRegInput, Problem[WebAuthnErrorDto], BeginRegOutput, Any] =
    webauthn
      .post
      .in("registration" / "options")
      .in(jsonIn[BeginRegInput])
      .out(jsonOut[BeginRegOutput])

//  val eFinishReg: PublicEndpoint[FinishRegInput, Problem[WebAuthnError], StatusCode, Any] =
//    webauthn
//      .post
//      .in("registration" / "verify")
//      .in(jsonIn[FinishRegInput])
//      .out(statusCode)

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
