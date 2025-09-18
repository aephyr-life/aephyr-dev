package aephyr.shared.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Nutrients(
    val kcal: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)
