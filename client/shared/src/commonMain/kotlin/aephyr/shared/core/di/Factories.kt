package aephyr.shared.core.di

import aephyr.shared.core.data.net.http.HttpFoodService
import aephyr.shared.core.domain.port.FoodService

object Factories {
    fun foodService(): FoodService =
        HttpFoodService(baseUrl = "https://world.openfoodfacts.net/api/v2/") // or OFF stub for now
}
