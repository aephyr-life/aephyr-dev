//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.shared.config

import java.util.Base64
import javax.crypto.spec.SecretKeySpec
import scala.util.Try

import zio.*
import zio.config.magnolia.*
import zio.config.typesafe.*

final case class MagicLinkIssuance(
                                    ttl: Duration,
                                    // HMAC key already decoded elsewhere or keep as base64url string:
                                    hmacSecretB64Url: SecretKeySpec
                                  )

final case class MagicLinkLinks(
                                 apiBaseUrl: BaseUrl,  // used for emails, e.g. https://api.example.com
                               )

final case class MagicLinkRedirects(
                                     successUrl: BaseUrl,  // where the browser gets redirected after consuming
                                     errorUrl: BaseUrl
                                   )

final case class SessionCookieCfg(
                                   name: String,
                                   domain: String,
                                   path: String,
                                   secure: Boolean,
                                   httpOnly: Boolean,
                                   sameSite: String,     // "lax" | "strict" | "none"
                                   maxAgeSec: Option[Int]
                                 )

final case class MagicLinkDelivery(
                                    from: String
                                    // smtp: SmtpCfg  // if/when you add real SMTP
                                  )

final case class MagicLinkCfg(
                               issuance: MagicLinkIssuance,
                               links: MagicLinkLinks,          // **API-oriented URLs for link building**
                               redirects: MagicLinkRedirects,  // **Browser flow**
                               cookie: SessionCookieCfg,       // **Browser flow**
                               delivery: MagicLinkDelivery     // email “from”, smtp etc.
                             )

object MagicLinkCfg:

  given Config[SecretKeySpec] = {
    Config.string.mapOrFail { s =>
      Try {
        new SecretKeySpec(Base64.getUrlDecoder.nn.decode(s), "HmacSHA256")
      }
        .toEither
        .left.map(t => Config.Error.InvalidData(Chunk.empty, t.getMessage.nn))
    }
  }
  
//  private val desc = deriveConfig[MagicLinkCfg].nested("app", "auth", "magic-link").mapKey(_.toKebabCase)
  
//  val layer: ZLayer[Any, Throwable, MagicLinkCfg] =
//    ZLayer.fromZIO(TypesafeConfigProvider.fromResourcePath().load(desc).mapError(new RuntimeException(_)))
