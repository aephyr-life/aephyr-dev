package aephyr.shared.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import aephyr.shared.foodlog.db.FoodDb

actual object DriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(FoodDb.Schema, "food.db")
}
