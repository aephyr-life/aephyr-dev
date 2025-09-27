package aephyr.shared.feature.foodlog.data

import aephyr.shared.feature.foodlog.model.*
import aephyr.shared.feature.foodlog.port.AddFoodLogItemCommand
import aephyr.shared.units.*
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.random.Random

@kotlin.time.ExperimentalTime
internal interface FoodLogLocalStore {
    fun observeDay(date: LocalDate): Flow<List<FoodLogItem>>
    suspend fun upsert(item: FoodLogItem, sourceId: FoodLogItemSourceId?, dirty: Boolean)
    suspend fun markDeleted(id: FoodLogItemId)
    suspend fun purge(id: FoodLogItemId)
}
