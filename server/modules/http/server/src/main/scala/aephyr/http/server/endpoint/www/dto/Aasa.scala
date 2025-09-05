package aephyr.http.server.endpoint.www.dto

import zio.json.JsonCodec

final case class Aasa(
  webcredentials: AasaWebCredentials
) derives JsonCodec
