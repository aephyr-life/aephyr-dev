package aephyr.shared.core.domain.port

import aephyr.shared.core.domain.model.Food

interface FoodService {
    suspend fun byBarcode(code: String): Food?
    suspend fun search(query: String): List<Food>
}
