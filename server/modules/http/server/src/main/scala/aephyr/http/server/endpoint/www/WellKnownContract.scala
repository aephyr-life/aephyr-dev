package aephyr.http.server.endpoint.www

import sttp.tapir.*
import sttp.tapir.json.jsoniter.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import dto.Aasa

object WellKnownContract {
  
  val aasa: PublicEndpoint[Unit, Unit, Aasa, Any] = 
    endpoint
      .get
      .in(".well-known" / "apple-app-site-association")
      .out(jsonBody[Aasa])
}
