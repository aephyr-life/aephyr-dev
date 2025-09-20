package aephyr.shared.feature.logfood.model

import kotlin.jvm.JvmInline
import kotlin.comparisons.compareValuesBy
import kotlin.math.roundToInt
import kotlinx.datetime.Instant

@JvmInline value class FoodLogId(val value: String) : Comparable<FoodLogId> {
    override fun compareTo(other: FoodLogId): Int = value.compareTo(other.value)
}
@JvmInline value class FoodName(val value: String) : Comparable<FoodName> {
    override fun compareTo(other: FoodName): Int = value.compareTo(other.value)
    override fun toString() = value
}

@JvmInline value class Decigram(val value: Int) {
    fun gram(): Double = value / 10.0
    operator fun plus(other: Decigram) = Decigram(value + other.value)
    companion object {
        fun fromGram(g: Double): Decigram = Decigram((g * 10).roundToInt())
        fun zero() = Decigram(0)
    }
}
@JvmInline value class Gram(val value: Int) {
    companion object {
        fun fromDouble(g: Double): Gram = Gram(g.roundToInt())
        fun zero() = Gram(0)
    }
}

@JvmInline value class KiloJoule(val value: Int) {
    fun kcal(): Int = (value / 4.184).roundToInt()
    operator fun plus(other: KiloJoule) = KiloJoule(value + other.value)
    companion object {
        fun fromKcal(kcal: Int): KiloJoule = KiloJoule((kcal * 4.184).roundToInt())
        fun zero() = KiloJoule(0)
    }
}

data class Macros(
    val protein: Decigram,
    val fat:     Decigram,
    val carb:    Decigram
) {

    operator fun plus(other: Macros) = Macros(
        protein = Decigram(this.protein.value + other.protein.value),
        fat     = Decigram(this.fat.value     + other.fat.value),
        carb    = Decigram(this.carb.value    + other.carb.value)
    )

    fun kjApprox(): KiloJoule {
        val kcal = protein.gram() * 4 + fat.gram() * 9 + carb.gram() * 4
        val kj = (kcal * 4.184).roundToInt()
        return KiloJoule(kj)
    }

    companion object {
        fun zero() = Macros(Decigram.zero(), Decigram.zero(), Decigram.zero())
    }
}



data class Notice(
    val severity: Severity,
    val code: String,        // e.g. "sodium_high", "protein_low", "missing_energy"
    val message: String      // short, user-friendly text
) {
    enum class Severity { Info, Warning, Alert }
}

sealed interface Quantity {
    data class Mass(val grams: Gram) : Quantity
}

fun Quantity.toGramsOrNull(): Gram? = when (this) {
    is Quantity.Mass -> grams
}

data class LoggedFood (
    val id: FoodLogId,
    val consumedAt: DietMoment,
    val name: FoodName,
    val portion: Quantity? = null,
    val energy: KiloJoule? = null,
    val macros: Macros? = null,
    val notices: List<Notice> = emptyList(),
    val loggedAt: Instant
) : Comparable<LoggedFood> {

    fun energyBestEffort(): KiloJoule? = energy ?: macros?.kjApprox()

    fun energyOrZero(): KiloJoule = energyBestEffort() ?: KiloJoule.zero()

    override fun compareTo(other: LoggedFood): Int =
        compareValuesBy(this, other,
            { it.consumedAt },
            { it.loggedAt },
            { it.name },
            { it.id }
        )
}
