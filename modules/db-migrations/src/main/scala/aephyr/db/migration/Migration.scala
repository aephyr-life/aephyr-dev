//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.db.migration

import org.flywaydb.core.Flyway

object Migration:
  @main def migrate(): Unit =
    val url  = sys.env("FLYWAY_URL")
    val user = sys.env("FLYWAY_USER")
    val pass = sys.env("FLYWAY_PASSWORD")
    Flyway
      .configure()
      .nn
      .dataSource(url, user, pass)
      .nn
      .schemas("events", "read", "tech")
      .nn
      .locations("classpath:db/migration")
      .nn
      .baselineOnMigrate(true)
      .nn
      .load()
      .nn
      .migrate(): Unit
