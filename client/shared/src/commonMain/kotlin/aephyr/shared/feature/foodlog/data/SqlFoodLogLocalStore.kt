package aephyr.shared.feature.foodlog.data

import aephyr.shared.foodlog.db.FoodDb
import aephyr.shared.feature.foodlog.model.*
import aephyr.shared.units.*
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import foodlog.FoodLogItemDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.time.Instant

@kotlin.time.ExperimentalTime
internal class SqlFoodLogLocalStore(
    private val db: FoodDb,
    private val io: CoroutineDispatcher,
    private val json: Json
) : FoodLogLocalStore {

    override fun observeDay(date: LocalDate): Flow<List<FoodLogItem>> =
        db.food_logQueries.getDay(date.toString())
            .asFlow()
            .mapToList(io)
            .map { rows -> rows.map { it.toDomain(json) } }

    override suspend fun upsert(item: FoodLogItem, sourceId: FoodLogItemSourceId?, dirty: Boolean) {
        db.food_logQueries.insertOrReplace(
            id = item.id.value,
            date = item.consumedAt.day.toString(),
            time_minutes = item.consumedAt.timeMinutes?.toLong(),
            name = item.name.value,
            grams = item.mass?.let { it.valueInGrams() },
            energy_kj = item.energy?.to(EnergyUnit.KILOJOULE),
            macros_json = item.macros?.let { json.encodeToString(it) },
            notices_json = if (item.notices.isEmpty()) null else json.encodeToString(item.notices),
            consumed_at_epoch_ms = item.loggedAtConsumingInstantMillis(), // choose: consumed vs logged; see below
            logged_at_epoch_ms = item.loggedAt.toEpochMilliseconds(),
            source_id = sourceId?.value,
            dirty = if (dirty) 1 else 0,
            deleted = 0
        )
    }

    override suspend fun markDeleted(id: FoodLogItemId) {
        db.food_logQueries.markDeleted(id.value)
    }

    override suspend fun purge(id: FoodLogItemId) {
        db.food_logQueries.purge(id.value)
    }
}

/** --- Mapping helpers --- */

@kotlin.time.ExperimentalTime
private fun FoodLogItemDb.toDomain(json: Json): FoodLogItem =
    FoodLogItem(
        id = FoodLogItemId(id),
        consumedAt = DietMoment(
            day = LocalDate.parse(date),
            timeMinutes = time_minutes?.toInt()
        ),
        name = FoodName(name),
        mass = grams?.let { Mass.g(it) },
        energy = energy_kj?.let { Energy.kJ(it) },
        macros = macros_json?.let { json.decodeFromString<Macros>(it) },
        notices = notices_json?.let { json.decodeFromString<List<Notice>>(it) } ?: emptyList(),
        loggedAt = Instant.fromEpochMilliseconds(logged_at_epoch_ms)
    )

/** Serialize Mass to grams (adjust if your Mass has units) */
private fun Mass.valueInGrams(): Double = when (unit) {
    // adapt if you support multiple units
    else -> value
}

/** Choose how you want 'consumedAt' epoch persisted. If you only have LocalDate + minutes, pick a timezone. */

@kotlin.time.ExperimentalTime
private fun FoodLogItem.loggedAtConsumingInstantMillis(): Long =
    // For demo: store 'consumedAt' as midnight + minutes in system TZ.
    DietMomentUtil.toEpochMillis(consumedAt)
