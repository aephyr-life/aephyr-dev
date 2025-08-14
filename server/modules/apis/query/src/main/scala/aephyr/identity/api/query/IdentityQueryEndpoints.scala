package aephyr.identity.api.query

import sttp.tapir.Endpoint

object IdentityQueryEndpoints:
  val all: List[Endpoint[?, ?, ?, ?, ?]] =
    List(
      MagicLinkQueryEndpoints.redeemMagicLink
    )
