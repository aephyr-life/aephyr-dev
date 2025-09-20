package aephyr.shared.ios.feature.logfood

import platform.Foundation.NSDate

data class IOSLoggedFood(
    val id: String,
    val name: String,
    val consumedDate: NSDate,
    val timeMinutes: Int?,
    val grams: Int?,
    val energyKJ: Int?,
    val energyKcalRounded: Int?,
    val protein_g: Double?,
    val fat_g: Double?,
    val carb_g: Double?
)
