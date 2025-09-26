package aephyr.shared.feature.foodlog.port

import aephyr.shared.feature.foodlog.data.FoodLogLocalStore
import aephyr.shared.feature.foodlog.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.random.Random
import kotlin.time.Clock

@kotlin.time.ExperimentalTime
internal class FoodLogPortImpl(
    private val store: FoodLogLocalStore
) : FoodLogPort {

    override fun observeDay(date: LocalDate): Flow<FoodLogDay> {
        return store.observeDay(date).map { items -> FoodLogDay.of(date, items) }
    }

    override suspend fun add(command: AddFoodLogItemCommand): FoodLogItem {
        val id = FoodLogItemId(genId())
        val consumedInstant = Instant.fromEpochMilliseconds(command.consumedAtMillis)
        val local = consumedInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val consumedAt = DietMoment(
            day = local.date,
            timeMinutes = local.hour * 60 + local.minute
        )

        val item = FoodLogItem(
            id = id,
            consumedAt = consumedAt,
            name = command.name,
            portion = command.grams,
            energy = command.energy,
            macros = command.macros,
            notices = emptyList(),
            loggedAt = Clock.System.now()
        )

        store.upsert(
            item = item,
            sourceId = command.sourceId,
            dirty = false // set true if you add a remote sync
        )
        return item
    }

    override suspend fun remove(id: FoodLogItemId) {
        // For pure local delete:
        store.purge(id)
        // If you plan to sync later, mark tombstone instead:
        // local.markDeleted(id)
    }

    private fun genId(): String =
        Random.nextBytes(8).joinToString("") { b ->
            (b.toInt() and 0xFF).toString(16).padStart(2, '0')
        }
}
