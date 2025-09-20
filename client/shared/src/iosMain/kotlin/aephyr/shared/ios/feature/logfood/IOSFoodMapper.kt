package aephyr.shared.ios.feature.logfood

import aephyr.shared.feature.logfood.logic.AggregateHero
import aephyr.shared.feature.logfood.model.*
import aephyr.shared.ios.util.IOSDateBridge
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

private fun roundKJToKcalStep(valueKJ: Int, stepKcal: Int): Int {
    val stepKJ = (stepKcal * 4.184).roundToInt().coerceAtLeast(1)
    return ((valueKJ + stepKJ / 2) / stepKJ) * stepKJ
}

fun LoggedFood.toDTO(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    roundingKcalStep: Int = 50
): IOSLoggedFood {
    val consumedDate = IOSDateBridge.localDateToNSDate(consumedAt.day, timeZone)
    val grams = (portion as? Quantity.Mass)?.grams?.value
    val energyBest = energy?.value ?: macros?.let {
        val kcal = it.protein.gram() * 4 + it.fat.gram() * 9 + it.carb.gram() * 4
        (kcal * 4.184).roundToInt()
    }
    val roundedKJ = energyBest?.let { roundKJToKcalStep(it, roundingKcalStep) }
    return IOSLoggedFood(
        id = id.value,
        name = name.value,
        consumedDate = consumedDate,
        timeMinutes = consumedAt.timeMinutes,
        grams = grams,
        energyKJ = energyBest,
        energyKcalRounded = roundedKJ?.let { (it / 4.184).roundToInt() },
        protein_g = macros?.protein?.gram(),
        fat_g     = macros?.fat?.gram(),
        carb_g    = macros?.carb?.gram()
    )
}

fun AggregateHero.toDTO(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): IOSAggregateHero =
    IOSAggregateHero(
        date = IOSDateBridge.localDateToNSDate(day, timeZone),
        totalEnergyKJ = totalEnergyKJ.value,
        totalEnergyKcalRounded = totalEnergyKcalRounded,
        protein_g = totalMacros.protein.gram().roundToInt(),
        fat_g = totalMacros.fat.gram().roundToInt(),
        carb_g = totalMacros.carb.gram().roundToInt(),
        protein_pct = proteinPct,
        fat_pct = fatPct,
        carb_pct = carbPct,
    )
