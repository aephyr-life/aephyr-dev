package aephyr.identity.api.command

import aephyr.api.ErrorDto
import aephyr.identity.domain.User
import aephyr.identity.domain.User.EmailAddress
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.jsoniter._

object MagicLinkCommandEndpoints:

  given Schema[EmailAddress] =
    Schema.string
      .format("email")
      .encodedExample("user@example.com")

  val requestMagicLink: Endpoint[
    Unit,
    MagicLinkCreationRequest,
    ErrorDto,
    MagicLinkCreationResponse,
    Any
  ] =
    endpoint.post
      .in("api" / "auth" / "link")
      .in(
        jsonBody[MagicLinkCreationRequest].example(
          MagicLinkCreationRequest(User.EmailAddress("user@example.com"))
        )
      )
      .out(
        jsonBody[MagicLinkCreationResponse].example(
          MagicLinkCreationResponse(
            "If an account exists for this email, a sign-in link has been sent."
          )
        )
      )
      .out(statusCode(StatusCode.Accepted))
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
      .description("Request a passwordless magic link to be sent via email")
      .name("request login link")
      .tag("auth-magiclink")
