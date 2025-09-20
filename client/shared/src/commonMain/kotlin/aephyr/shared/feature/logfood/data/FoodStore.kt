package aephyr.shared.feature.logfood.data

import kotlinx.datetime.LocalDate
import aephyr.shared.feature.logfood.model.LoggedFood

interface FoodStore {
    suspend fun entriesFor(day: LocalDate): List<LoggedFood>
}
