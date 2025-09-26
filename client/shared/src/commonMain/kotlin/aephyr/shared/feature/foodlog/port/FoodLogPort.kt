package aephyr.shared.feature.foodlog.port

import aephyr.shared.feature.foodlog.model.FoodLogDay
import aephyr.shared.feature.foodlog.model.FoodLogItem
import aephyr.shared.feature.foodlog.model.FoodLogItemId
import kotlinx.coroutines.flow.Flow


@kotlin.time.ExperimentalTime
interface FoodLogPort {
    /** Continuous updates for a given day (local-first). */
    fun observeDay(dayKey: String): Flow<FoodLogDay>

    /** Add an item (optimistic local insert + enqueue sync). */
    suspend fun add(command: AddFoodLogItemCommand): FoodLogItem

    /** Remove an item by id (optimistic local delete + enqueue sync). */
    suspend fun remove(id: FoodLogItemId)
}
