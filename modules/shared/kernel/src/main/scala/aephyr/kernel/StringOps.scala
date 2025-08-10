//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.kernel

object StringOps:
  extension (s: String)
    def camelToKebab: String =
      s.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase
