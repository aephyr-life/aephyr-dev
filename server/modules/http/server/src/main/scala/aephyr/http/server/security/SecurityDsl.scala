package aephyr.http.server.security

import zio.*
import sttp.tapir.ztapir.*
import sttp.tapir.Schema
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import aephyr.api.TapirDsl
import aephyr.api.shared.{ AuthenticationContext, Problem }
import aephyr.security.Principal

type AuthEnv = AuthService

object SecurityDsl {

  /** Standard bearer-secured base endpoint with centralized auth. */
  def secureBase[E](using
    sch: Schema[E],
    json: JsonValueCodec[E],
    map: AuthErrorToProblem[E]
  ): ZPartialServerEndpoint[
    AuthEnv,
    AuthenticationContext,
    Principal,
    Unit, // INPUT (rest of inputs added later)
    Problem[E],
    Unit, // OUTPUT (added later by .out/.serverLogic)
    Any // Caps
  ] =
    TapirDsl
      .secureBaseBearer[E](using sch, json) // <-- explicitly forward givens
      .zServerSecurityLogic {
        (authCtx: AuthenticationContext) =>
          ZIO
            .serviceWithZIO[AuthService](_.authenticate(authCtx))
            .mapError(
              err => map(err)
            )
      }
}
