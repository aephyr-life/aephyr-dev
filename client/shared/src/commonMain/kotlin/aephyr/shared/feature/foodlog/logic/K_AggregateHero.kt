package aephyr.shared.feature.foodlog.logic

import aephyr.shared.feature.foodlog.model.*
import aephyr.shared.units.K_Energy
import kotlinx.datetime.LocalDate

data class K_AggregateHero(
    val day: LocalDate,

    // Energy
    val totalEnergy: K_Energy,

    // Macros
    val totalMacros: K_Macros,

    val proteinPct: Double,
    val fatPct: Double,
    val carbPct: Double,
)
