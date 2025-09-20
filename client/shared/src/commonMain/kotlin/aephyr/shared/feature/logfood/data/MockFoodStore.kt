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
    private val data = demoSeed()

    private fun Byte.toHex(): String =
        this.toUByte().toString(16).padStart(2, '0')

    private fun ByteArray.toHex(): String =
        joinToString("") { it.toHex() }

    fun newId(bytes: Int = 8): FoodLogId =
        FoodLogId(kotlin.random.Random.nextBytes(bytes).toHex())

    override suspend fun add(input: NewLogInput): LoggedFood {
        val id = newId()
        val logged = LoggedFood(
            id = id,
            consumedAt = input.consumedAt,                     // <<< use it
            name = input.name,
            portion = input.portion ?: Quantity.Mass(Gram.zero()),
            energy = input.energy ?: input.macros?.let {
                val kcal = it.protein.gram()*4 + it.fat.gram()*9 + it.carb.gram()*4
                KiloJoule.fromKcal(kotlin.math.round(kcal).toInt())
            } ?: KiloJoule.zero(),
            macros = input.macros ?: Macros.zero(),
            notices = emptyList(),
            loggedAt = kotlinx.datetime.Clock.System.now()
        )
        val dayKey = input.consumedAt.day
        data.getOrPut(dayKey) { mutableListOf() }.add(logged)
        return logged
    }

    override suspend fun remove(id: FoodLogId): Boolean {
        var removed = false
        for ((day, list) in data) {
            val before = list.size
            list.removeAll { it.id == id }
            if (list.size != before) {
                removed = true
                if (list.isEmpty()) data.remove(day)
                break
            }
        }
        return removed
    }

    override suspend fun entriesFor(day: LocalDate): List<LoggedFood> =
        (data[day] ?: mutableListOf()).sorted()
}

private fun newId(): FoodLogId {
    val r = Random.Default
    val part = (1..8).map { ('a'..'f').random(r) }.joinToString("")
    return FoodLogId("log-$part")
}

fun demoSeed(): MutableMap<LocalDate, MutableList<LoggedFood>> {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(tz).date
    fun day(n: Int) = today.minus(DatePeriod(days = n))
    fun dietMoment(day: LocalDate, hour: Int, minute: Int) = DietMoment(day, hour * 60 + minute)

    fun log(
        day: LocalDate, hour: Int, minute: Int,
        name: String, grams: Int,
        kjPer100: Int, protein_g_per100: Double, fat_g_per100: Double, carb_g_per100: Double
    ): LoggedFood {
        val factor = grams / 100.0
        return LoggedFood(
            id = newId(),
            consumedAt = dietMoment(day, hour, minute),
            name = FoodName(name),
            portion = Quantity.Mass(Gram(grams)),
            energy = KiloJoule((kjPer100 * factor).toInt()),
            macros = Macros(
                protein = Decigram.fromGram(protein_g_per100 * factor),
                fat     = Decigram.fromGram(fat_g_per100 * factor),
                carb    = Decigram.fromGram(carb_g_per100 * factor)
            ),
            loggedAt = now
        )
    }

    val rnd = Random(42)
    // Linked map keeps insertion order of days
    val byDay: MutableMap<LocalDate, MutableList<LoggedFood>> = linkedMapOf(
        day(0) to mutableListOf(),
        day(1) to mutableListOf(),
        day(2) to mutableListOf(), // empty day present
        day(3) to mutableListOf(),
        day(4) to mutableListOf()
    )

    fun add(d: LocalDate, item: LoggedFood) { byDay.getValue(d).add(item) }

    add(day(0), log(day(0), 8, 30, "Oatmeal", 60, 1627, 16.6, 6.9, 66.7))
    add(day(0), log(day(0), 8, 30, "Milk 1.5%", 200, 198, 3.4, 1.5, 4.8))
    add(day(0), log(day(0), 13, 15, "Chicken Breast", 150, 450, 31.0, 3.6, 0.0))
    add(day(0), log(day(0), 13, 15, "Rice (cooked)", 180, 600, 2.7, 0.3, 28.0))

    add(day(1), log(day(1), 9, 0, "Scrambled Eggs", 120, 600, 12.5, 10.5, 1.0))
    add(day(1), log(day(1), 19, 0, "Salmon Fillet", 180, 820, 22.0, 13.0, 0.0))

    // day(2) intentionally empty

    add(day(3), log(day(3), 7, 30, "Greek Yogurt 2%", 200, 255, 10.0, 6.8, 9.4))
    add(day(3), log(day(3), 7, 30, "Granola", 50, 1900, 10.0, 8.0, 70.0))
    add(day(3), log(day(3), 12, 45, "Pasta", 200, 630, 5.5, 1.5, 30.0))
    add(day(3), log(day(3), 12, 45, "Tomato Sauce", 100, 280, 2.0, 1.0, 6.0))
    add(day(3), log(day(3), 18, 30, "Beef Steak", 220, 800, 27.0, 8.0, 0.0))
    add(day(3), log(day(3), 20, 0, "Red Wine", 150, 350, 0.1, 0.0, 2.6))

    add(day(4), log(day(4), 10, 0, "Protein Shake", 30, 1600, 80.0, 5.0, 5.0))

    // (Optional) scramble per-day order to test sorting later
    byDay.values.forEach { it.shuffle(rnd) }

    return byDay
}
