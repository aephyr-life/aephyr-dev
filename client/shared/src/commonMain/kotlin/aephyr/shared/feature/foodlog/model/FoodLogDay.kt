package aephyr.shared.feature.foodlog.model

import kotlinx.datetime.LocalDate

@kotlin.time.ExperimentalTime
class FoodLogDay(
    val date: LocalDate,
    val items: List<FoodLogItem>
) {
    companion object {
        fun of(date: LocalDate, items: List<FoodLogItem>): FoodLogDay {
            require(items.all { it.consumedAt.day == date }) {
                "All items must belong to $date"
            }
            // Optional: stable ordering within the day (null time last)
            val sorted = items.sortedWith(
                compareBy<FoodLogItem> { it.consumedAt.timeMinutes ?: Int.MAX_VALUE }
                    .thenBy { it.name }
                    .thenBy { it.id }
            )
            return FoodLogDay(date, sorted)
        }
    }
}
