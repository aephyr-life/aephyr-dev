package aephyr.http.server.auth

import aephyr.kernel.id.UserId
import zio.*

trait AuthExtractor {
  def extract: ZIO[Any, ExtractionError, Option[UserId]]
}

sealed trait ExtractionError extends Throwable
case object MissingAuthHeader extends ExtractionError
case object InvalidToken extends ExtractionError
