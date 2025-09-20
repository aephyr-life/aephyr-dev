package aephyr.shared.feature.logfood.data

import aephyr.shared.feature.logfood.logic.AggregateHero
import aephyr.shared.feature.logfood.logic.Aggregates
import kotlinx.datetime.LocalDate

suspend fun FoodStore.aggregateHero(
    day: LocalDate,
    roundingKcalStep: Int = 50
): AggregateHero = Aggregates.hero(day, entriesFor(day), roundingKcalStep)
