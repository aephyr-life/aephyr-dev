package aephyr.shared.feature.foodlog.logic

import kotlin.math.roundToInt
import aephyr.shared.feature.foodlog.model.*
import aephyr.shared.units.K_Energy
import kotlinx.datetime.LocalDate

object K_Aggregates {
    /** Build the dashboard hero for a day. */
    fun hero(
        day: LocalDate,
        entries: List<K_LoggedFood>,
        roundingKcalStep: Int = 50
    ): K_AggregateHero {

        val macros = entries.fold(K_Macros.ZERO) { acc, e ->
            val m = e.macros ?: return@fold acc
            acc + m
        }

        fun zeroSafeDiv(a: K_Energy, b: K_Energy) =
            if (b.value.equals(0.0)) 0.0
            else a / b

        return K_AggregateHero(
            day = day,
            totalEnergy = macros.totalEnergy,
            totalMacros = macros,
            proteinPct = zeroSafeDiv(macros.proteinEnergy, macros.totalEnergy),
            fatPct = zeroSafeDiv(macros.fatEnergy, macros.totalEnergy),
            carbPct = zeroSafeDiv(macros.carbEnergy, macros.totalEnergy),
        )
    }
}
