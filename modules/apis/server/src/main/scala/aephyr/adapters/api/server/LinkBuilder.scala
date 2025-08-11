//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.adapters.api.server

import sttp.model.Uri
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.tapir._
import aephyr.identity.api.query.MagicLinkQueryEndpoints

final class LinkBuilder(baseUrl: String) {
  
  private val base = Uri.unsafeParse(baseUrl)
  private val interp = SttpClientInterpreter()
  private val magicReq = interp.toRequest(MagicLinkQueryEndpoints.consumeMagicLink, Some(base))
  
  def magicLink(token: String): String = {
    magicReq(token).uri.toString
  }
}