package aephyr.shared.feature.foodlog.model

import kotlinx.datetime.LocalDate

data class DietMoment(
    val day: LocalDate,
    val timeMinutes: Int? // optional
) : Comparable<DietMoment> {

    fun isSameDay(other: DietMoment) = day == other.day

    override fun compareTo(other: DietMoment): Int {
        // Compare by day first
        val dayCompare = day.compareTo(other.day)
        if (dayCompare != 0) return dayCompare

        // Then compare by timeMinutes, putting "no time" last
        val t1 = timeMinutes ?: Int.MAX_VALUE
        val t2 = other.timeMinutes ?: Int.MAX_VALUE
        return t1.compareTo(t2)
    }
}
