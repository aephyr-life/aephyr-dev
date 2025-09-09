package aephyr.api.shared

import aephyr.security.Bearer

final case class AuthenticationContext(
  bearer: Option[Bearer],
  request: RequestContext
)
