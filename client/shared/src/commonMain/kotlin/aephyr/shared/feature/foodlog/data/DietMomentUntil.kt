package aephyr.shared.feature.foodlog.data

import aephyr.shared.feature.foodlog.model.DietMoment
import kotlinx.datetime.*

@kotlin.time.ExperimentalTime
internal object DietMomentUtil {
    fun toEpochMillis(m: DietMoment, tz: TimeZone = TimeZone.currentSystemDefault()): Long {
        val time = m.timeMinutes?.let { it } ?: 0
        val h = time / 60
        val min = time % 60
        val ldt = LocalDateTime(m.day.year, m.day.month, m.day.day, h, min)
        return ldt.toInstant(tz).toEpochMilliseconds()
    }
}
