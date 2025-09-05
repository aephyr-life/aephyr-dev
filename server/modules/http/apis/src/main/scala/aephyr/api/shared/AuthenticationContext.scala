package aephyr.api.shared

import aephyr.security.Bearer

final case class AuthenticationContext(
  bearer: Bearer,
  request: RequestContext
)
