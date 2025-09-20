package aephyr.shared.ios.feature.logfood

import platform.Foundation.NSDate

data class IOSAggregateHero(
    val date: NSDate,
    val totalEnergyKJ: Int,
    val totalEnergyKcalRounded: Int,
    val protein_g: Int,
    val protein_pct: Double,
    val fat_g: Int,
    val fat_pct: Double,
    val carb_g: Int,
    val carb_pct: Double,
)
