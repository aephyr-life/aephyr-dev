//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.kernel

import StringOps.*
import zio.test.*
import zio.test.Assertion.*

object StringOpsSpec extends ZIOSpecDefault {

  override def spec =
    suite("StringOps")(
      test("happy path") {
        assert("DbPoolSize".camelToKebab)(equalTo("db-pool-size"))
      }
    )
}

