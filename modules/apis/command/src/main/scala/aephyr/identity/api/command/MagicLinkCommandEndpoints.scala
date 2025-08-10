package aephyr.identity.api.command

import aephyr.identity.domain.User
import aephyr.identity.domain.User.EmailAddress
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.jsoniter.*

object MagicLinkCommandEndpoints:

  given Schema[EmailAddress] =
    Schema.string
      .format("email")
      .encodedExample("user@example.com")

  val requestMagicLink: Endpoint[Unit, MagicLinkCreationRequest, Unit, MagicLinkCreationResponse, Any] =
    endpoint.post
      .in("api" / "auth" / "magic-link")
      .in(jsonBody[MagicLinkCreationRequest].example(MagicLinkCreationRequest(User.EmailAddress("user@example.com"))))
      .out(jsonBody[MagicLinkCreationResponse])
      .description("Request a passwordless magic link to be sent via email")
      .name("requestMagicLink")
