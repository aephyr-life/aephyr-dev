package aephyr.shared.ios.util

import kotlinx.datetime.*

/**
 * Convert an Instant (e.g., from Clock.System.now()) to LocalDate
 * using the given time zone.
 */
fun Instant.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    this.toLocalDateTime(timeZone).date


fun shiftDay(base: LocalDate, days: Int): LocalDate =
    base.plus(DatePeriod(days = days))

/**
 * Convenience: current local date in the system timezone.
 */
fun currentLocalDate(): LocalDate =
    Clock.System.now().toLocalDate()
