package aephyr.shared.feature.logfood.data

import kotlinx.datetime.LocalDate
import aephyr.shared.feature.logfood.model.FoodLogId
import aephyr.shared.feature.logfood.model.LoggedFood
import aephyr.shared.feature.logfood.model.NewLogInput

interface FoodStore {
    suspend fun add(input: NewLogInput): LoggedFood
    suspend fun remove(id: FoodLogId): Boolean
    suspend fun entriesFor(day: LocalDate): List<LoggedFood>
}
