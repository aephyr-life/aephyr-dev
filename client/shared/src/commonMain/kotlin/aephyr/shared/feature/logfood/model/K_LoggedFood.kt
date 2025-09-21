package aephyr.shared.feature.logfood.model

import kotlin.jvm.JvmInline
import kotlin.comparisons.compareValuesBy
import kotlin.math.roundToInt
import kotlinx.datetime.Instant

@JvmInline value class K_FoodLogId(val value: String) : Comparable<K_FoodLogId> {
    override fun compareTo(other: K_FoodLogId): Int = value.compareTo(other.value)
}

@JvmInline value class K_FoodName(val value: String) : Comparable<K_FoodName> {
    override fun compareTo(other: K_FoodName): Int = value.compareTo(other.value)
    override fun toString() = value
}

@JvmInline value class K_Decigram(val value: Int) {
    fun gram(): Double = value / 10.0
    operator fun plus(other: K_Decigram) = K_Decigram(value + other.value)
    companion object {
        fun fromGram(g: Double): K_Decigram = K_Decigram((g * 10).roundToInt())
        fun zero() = K_Decigram(0)
    }
}
@JvmInline value class K_Gram(val value: Int) {
    companion object {
        fun fromDouble(g: Double): K_Gram = K_Gram(g.roundToInt())
        fun zero() = K_Gram(0)
    }
}

@JvmInline value class K_KiloJoule(val value: Int) {
    fun kcal(): Int = (value / 4.184).roundToInt()
    operator fun plus(other: K_KiloJoule) = K_KiloJoule(value + other.value)
    companion object {
        fun fromKcal(kcal: Int): K_KiloJoule = K_KiloJoule((kcal * 4.184).roundToInt())
        fun zero() = K_KiloJoule(0)
    }
}

data class K_Macros(
    val protein: K_Decigram,
    val fat:     K_Decigram,
    val carb:    K_Decigram
) {

    operator fun plus(other: K_Macros) = K_Macros(
        protein = K_Decigram(this.protein.value + other.protein.value),
        fat     = K_Decigram(this.fat.value     + other.fat.value),
        carb    = K_Decigram(this.carb.value    + other.carb.value)
    )

    fun kjApprox(): K_KiloJoule {
        val kcal = protein.gram() * 4 + fat.gram() * 9 + carb.gram() * 4
        val kj = (kcal * 4.184).roundToInt()
        return K_KiloJoule(kj)
    }

    companion object {
        fun zero() = K_Macros(K_Decigram.zero(), K_Decigram.zero(), K_Decigram.zero())
    }
}

data class K_Notice(
    val severity: Severity,
    val code: String,        // e.g. "sodium_high", "protein_low", "missing_energy"
    val message: String      // short, user-friendly text
) {
    enum class Severity { Info, Warning, Alert }
}

sealed interface K_Quantity {
    data class Mass(val grams: K_Gram) : K_Quantity
}

fun K_Quantity.toGramsOrNull(): K_Gram? = when (this) {
    is K_Quantity.Mass -> grams
}

data class K_LoggedFood (
    val id: K_FoodLogId,
    val consumedAt: K_DietMoment,
    val name: K_FoodName,
    val portion: K_Quantity? = null,
    val energy: K_KiloJoule? = null,
    val macros: K_Macros? = null,
    val notices: List<K_Notice> = emptyList(),
    val loggedAt: Instant
) : Comparable<K_LoggedFood> {

    fun energyBestEffort(): K_KiloJoule? = energy ?: macros?.kjApprox()

    fun energyOrZero(): K_KiloJoule = energyBestEffort() ?: K_KiloJoule.zero()

    override fun compareTo(other: K_LoggedFood): Int =
        compareValuesBy(this, other,
            { it.consumedAt },
            { it.loggedAt },
            { it.name },
            { it.id }
        )
}
