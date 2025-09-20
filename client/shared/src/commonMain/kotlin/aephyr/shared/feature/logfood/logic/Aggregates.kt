package aephyr.shared.feature.logfood.logic

import kotlin.math.roundToInt
import aephyr.shared.feature.logfood.model.*
import kotlinx.datetime.LocalDate

object Aggregates {
    /** Build the dashboard hero for a day. */
    fun hero(
        day: LocalDate,
        entries: List<LoggedFood>,
        roundingKcalStep: Int = 50
    ): AggregateHero {
        // Sum best-effort energy (use stored kJ, else Atwater from macros; else 0)
        val totalEnergyKJValue = entries.fold(0) { acc, e ->
            acc + (e.energy?.value ?: e.macros?.let(::macrosToKJ) ?: 0)
        }
        val totalEnergyKJ = KiloJoule(totalEnergyKJValue)

        // Macros total (null-safe)
        val totalMacros = entries.fold(Macros.zero()) { acc, e ->
            val m = e.macros ?: return@fold acc
            Macros(
                protein = Decigram(acc.protein.value + m.protein.value),
                fat = Decigram(acc.fat.value + m.fat.value),
                carb = Decigram(acc.carb.value + m.carb.value)
            )
        }

        // Macro kcal contributions for % breakdown
        val protKcal = totalMacros.protein.gram() * 4
        val fatKcal = totalMacros.fat.gram() * 9
        val carbKcal = totalMacros.carb.gram() * 4
        val kcalForPct = protKcal + fatKcal + carbKcal
        val proteinPct = if (kcalForPct > 0.0) (protKcal / kcalForPct) else 0.0
        val fatPct = if (kcalForPct > 0.0) (fatKcal / kcalForPct)  else 0.0
        val carbPct = if (kcalForPct > 0.0) (carbKcal / kcalForPct)  else 0.0

        // Rounded totals for display (e.g. nearest 50 kcal)
        val roundedKJ = roundKJ(totalEnergyKJValue, roundingKcalStep)
        val roundedKcal = (roundedKJ / 4.184).roundToInt()

        val missingEnergyCount = entries.count { it.energy == null && it.macros == null }

        return AggregateHero(
            day = day,
            //entryCount = entries.size,
            totalEnergyKJ = totalEnergyKJ,
            totalEnergyKJRounded = KiloJoule(roundedKJ),
            totalEnergyKcalRounded = roundedKcal,
            totalMacros = totalMacros,
            proteinPct = proteinPct,
            fatPct = fatPct,
            carbPct = carbPct,
            //missingEnergyCount = missingEnergyCount
        )
    }

    private fun macrosToKJ(m: Macros): Int {
        val kcal = m.protein.gram() * 4 + m.fat.gram() * 9 + m.carb.gram() * 4
        return (kcal * 4.184).roundToInt()
    }

    /** Round kJ total to a kcal step (e.g., 50 kcal) to avoid fake precision. */
    private fun roundKJ(valueKJ: Int, stepKcal: Int): Int {
        val stepKJ = (stepKcal * 4.184).roundToInt().coerceAtLeast(1)
        return ((valueKJ + stepKJ / 2) / stepKJ) * stepKJ
    }
}
