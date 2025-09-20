package aephyr.shared.feature.logfood.data

import aephyr.shared.feature.logfood.model.LoggedFood
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import aephyr.shared.feature.logfood.model.*
import kotlinx.datetime.*

class MockFoodStore : FoodStore {
    private val seed: List<LoggedFood> = demoSeed()

    override suspend fun entriesFor(day: LocalDate): List<LoggedFood> =
        seed.filter { it.consumedAt.day == day }
}

private fun newId(): FoodLogId {
    val r = Random.Default
    val part = (1..8).map { ('a'..'f').random(r) }.joinToString("")
    return FoodLogId("log-$part")
}

fun demoSeed(): List<LoggedFood> {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(tz).date

    fun day(n: Int) = today.minus(DatePeriod(days = n))

    fun dietMoment(day: LocalDate, hour: Int, minute: Int): DietMoment =
        DietMoment(day, hour * 60 + minute)

    fun log(
        day: LocalDate,
        hour: Int,
        minute: Int,
        name: String,
        grams: Int,
        kjPer100: Int,
        protein_g_per100: Double,
        fat_g_per100: Double,
        carb_g_per100: Double
    ): LoggedFood {
        val gramsVal = Gram(grams)
        val factor = grams / 100.0
        val energy = KiloJoule((kjPer100 * factor).toInt())
        val macros = Macros(
            protein = Decigram.fromGram(protein_g_per100 * factor),
            fat = Decigram.fromGram(fat_g_per100 * factor),
            carb = Decigram.fromGram(carb_g_per100 * factor)
        )
        return LoggedFood(
            id = newId(),
            consumedAt = dietMoment(day, hour, minute),
            name = FoodName(name),
            portion = Quantity.Mass(gramsVal),
            energy = energy,
            macros = macros,
            loggedAt = now
        )
    }

    val rnd = Random(42)

    return buildList {
        // --- Day 0 (today): 4 entries ---
        add(log(day(0), 8, 30, "Oatmeal", 60, 1627, 16.6, 6.9, 66.7))
        add(log(day(0), 8, 30, "Milk 1.5%", 200, 198, 3.4, 1.5, 4.8))
        add(log(day(0), 13, 15, "Chicken Breast", 150, 450, 31.0, 3.6, 0.0))
        add(log(day(0), 13, 15, "Rice (cooked)", 180, 600, 2.7, 0.3, 28.0))

        // --- Day 1: 2 entries ---
        add(log(day(1), 9, 0, "Scrambled Eggs", 120, 600, 12.5, 10.5, 1.0))
        add(log(day(1), 19, 0, "Salmon Fillet", 180, 820, 22.0, 13.0, 0.0))

        // --- Day 2: 0 entries (empty day) ---

        // --- Day 3: 6 entries (heavier day) ---
        add(log(day(3), 7, 30, "Greek Yogurt 2%", 200, 255, 10.0, 6.8, 9.4))
        add(log(day(3), 7, 30, "Granola", 50, 1900, 10.0, 8.0, 70.0))
        add(log(day(3), 12, 45, "Pasta", 200, 630, 5.5, 1.5, 30.0))
        add(log(day(3), 12, 45, "Tomato Sauce", 100, 280, 2.0, 1.0, 6.0))
        add(log(day(3), 18, 30, "Beef Steak", 220, 800, 27.0, 8.0, 0.0))
        add(log(day(3), 20, 0, "Red Wine", 150, 350, 0.1, 0.0, 2.6))

        // --- Day 4: 1 entry ---
        add(log(day(4), 10, 0, "Protein Shake", 30, 1600, 80.0, 5.0, 5.0))
    }.shuffled(rnd) // shuffle to test sorting by DietMoment
}
