//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.kernel

import java.time.Duration

object DurationOps:

  extension (s: Duration) def minutes: Long = s.getSeconds / 60
