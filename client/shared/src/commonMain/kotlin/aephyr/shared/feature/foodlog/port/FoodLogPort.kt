package aephyr.shared.feature.foodlog.port

import aephyr.shared.feature.foodlog.model.FoodLogDay
import aephyr.shared.feature.foodlog.model.FoodLogItem
import aephyr.shared.feature.foodlog.model.FoodLogItemId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesRefined

@kotlin.time.ExperimentalTime
interface FoodLogPort {
    /** Continuous updates for a given day (local-first). */
    @NativeCoroutines
    fun observeDay(date: LocalDate): Flow<FoodLogDay>

    /** Add an item (optimistic local insert + enqueue sync). */
    @NativeCoroutines
    suspend fun add(command: AddFoodLogItemCommand): FoodLogItem

    /** Remove an item by id (optimistic local delete + enqueue sync). */
    @NativeCoroutines
    suspend fun remove(id: FoodLogItemId)
}
