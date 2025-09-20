package aephyr.shared.ios.util

import platform.Foundation.NSDate
import kotlinx.datetime.*
import platform.Foundation.dateWithTimeIntervalSince1970
import kotlin.math.roundToLong
import platform.Foundation.timeIntervalSinceDate

object IOSDateBridge {

    fun localDateToNSDate(
        date: LocalDate,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): NSDate {
        val start = date.atTime(LocalTime(0, 0))
        val instant = start.toInstant(timeZone)
        return NSDate.dateWithTimeIntervalSince1970(instant.toEpochMilliseconds().toDouble() / 1000.0)
    }

    fun nsDateToLocalDate(
        date: NSDate,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): LocalDate {
        // seconds since Unix epoch using a known reference date
        val seconds = date.timeIntervalSinceDate(NSDate.dateWithTimeIntervalSince1970(0.0))
        val ms = (seconds * 1000.0).roundToLong()
        return Instant.fromEpochMilliseconds(ms).toLocalDate(timeZone)
    }

    fun shiftDay(
        date: NSDate,
        days: Int
    ): NSDate {
        val timeZone = TimeZone.currentSystemDefault()
        val d0 = nsDateToLocalDate(date, timeZone)
        val d1 = d0.plus(DatePeriod(days = days))
        return localDateToNSDate(d1, timeZone)
    }
}
