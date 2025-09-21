package aephyr.shared.feature.logfood.data

import kotlinx.datetime.LocalDate
import aephyr.shared.feature.logfood.model.K_FoodLogId
import aephyr.shared.feature.logfood.model.K_LoggedFood
import aephyr.shared.feature.logfood.model.K_NewLogInput

interface K_FoodStore {
    suspend fun add(input: K_NewLogInput): K_LoggedFood
    suspend fun remove(id: K_FoodLogId): Boolean
    suspend fun entriesFor(day: LocalDate): List<K_LoggedFood>
}
