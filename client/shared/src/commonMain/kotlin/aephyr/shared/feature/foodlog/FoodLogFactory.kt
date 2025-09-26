package aephyr.shared.feature.foodlog

import aephyr.shared.foodlog.db.FoodDb
import aephyr.shared.data.db.DriverFactory
import aephyr.shared.feature.foodlog.data.SqlFoodLogLocalStore
import aephyr.shared.feature.foodlog.port.FoodLogPort
import aephyr.shared.feature.foodlog.port.FoodLogPortImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

@kotlin.time.ExperimentalTime
object FoodLogFactory {
    fun local(): FoodLogPort {
        val driver = DriverFactory.createDriver()
        val db = FoodDb(driver)
        val store = SqlFoodLogLocalStore(
            db = db,
            io = Dispatchers.Default,
            json = Json { ignoreUnknownKeys = true }   // or: Json.Default
        )
        return FoodLogPortImpl(store)
    }
}
