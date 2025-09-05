package aephyr.api.shared

import aephyr.kernel.telemetry.TraceId

final case class RequestContext(
  traceId: Option[TraceId],
  ipAddress: Option[IpAddress],
  userAgent: Option[UserAgent]
)
