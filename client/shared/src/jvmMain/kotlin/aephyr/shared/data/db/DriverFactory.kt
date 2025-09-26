package aephyr.shared.data.db

import aephyr.shared.foodlog.db.FoodDb
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual object DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:food.db")
        // For brand-new DBs you must create the schema once on JVM:
        FoodDb.Schema.create(driver)
        return driver
    }
}
