package aephyr.identity.api.query

import aephyr.api.ErrorDto

import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.jsoniter.*
import sttp.model.StatusCode

object MagicLinkQueryEndpoints:
  val consumeMagicLink: Endpoint[Unit, String, ErrorDto, MagicLinkConsumptionResponse, Any] =
    endpoint.get
      .in("api" / "auth" / "callback")
      .in(query[String]("token")) // TODO replace String with opaque type
      .errorOut(jsonBody[ErrorDto])
      .out(jsonBody[MagicLinkConsumptionResponse])
      .description("Login via a magic link")
      .name("consumeMagicLink")
