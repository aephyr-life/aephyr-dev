package aephyr.http.server.endpoint.www.dto

import zio.json.JsonCodec

final case class AasaWebCredentials(
  apps: List[String]
) derives JsonCodec
