package aephyr.http.server.security

import aephyr.identity.domain.auth.AuthError
import aephyr.api.shared.Problem

trait AuthErrorToProblem[E] {
  def apply(err: AuthError): Problem[E]
}
object AuthErrorToProblem {
  def default[E](title: String, status: Int = 401): AuthErrorToProblem[E] =
    (err: AuthError) => Problem[E](
      `type` = "about:blank",
      title  = title,
      status = status,
      detail = Some(err.toString),
      traceId = None, instance = None, error = None
    )
}
