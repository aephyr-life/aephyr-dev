package aephyr.shared.feature.logfood.data

import aephyr.shared.feature.logfood.logic.K_AggregateHero
import aephyr.shared.feature.logfood.logic.K_Aggregates
import kotlinx.datetime.LocalDate

suspend fun K_FoodStore.aggregateHero(
    day: LocalDate,
    roundingKcalStep: Int = 50
): K_AggregateHero = K_Aggregates.hero(day, entriesFor(day), roundingKcalStep)
