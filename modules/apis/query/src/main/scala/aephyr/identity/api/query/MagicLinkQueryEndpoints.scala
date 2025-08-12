package aephyr.identity.api.query

import aephyr.api.ErrorDto
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.jsoniter._

object MagicLinkQueryEndpoints:
  val redeemMagicLink
    : PublicEndpoint[String, ErrorDto, MagicLinkConsumptionResponse, Any] =
    endpoint.get
      .in(
        "api" / "auth" / "link" / path[String]("token")
      ) // TODO replace String with opaque type "LoginToken"
      .out(
        jsonBody[MagicLinkConsumptionResponse]
          .description("Accepted")
          .example(
            MagicLinkConsumptionResponse(
              "access granted."
            )
          )
      )
      .errorOut(
        oneOf[ErrorDto](
          oneOfVariant(
            StatusCode.BadRequest,
            jsonBody[ErrorDto]
              .description("Invalid payload")
          ),
          oneOfVariant(
            StatusCode.TooManyRequests,
            jsonBody[ErrorDto]
              .description("Rate limited")
          ),
          oneOfVariant(
            StatusCode.InternalServerError,
            jsonBody[ErrorDto]
              .description("Unexpected error")
          )
        )
      )
      .out(statusCode(StatusCode.Accepted))
      .description("Login via a magic link")
      .name("redeem login token")
      .tag("auth-magiclink")
