package aephyr.shared.feature.logfood.logic

import aephyr.shared.feature.logfood.model.*
import kotlinx.datetime.LocalDate

data class K_AggregateHero(
    val day: LocalDate,

    // Energy
    val totalEnergyKJ: K_KiloJoule,          // best-effort sum
    val totalEnergyKJRounded: K_KiloJoule,   // rounded for display, e.g. to nearest 50 kcal
    val totalEnergyKcalRounded: Int,       // convenience for UI

    // Macros
    val totalMacros: K_Macros,

    val proteinPct: Double,
    val fatPct: Double,
    val carbPct: Double,
)
