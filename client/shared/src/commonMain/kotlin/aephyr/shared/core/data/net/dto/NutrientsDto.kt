package aephyr.shared.core.data.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class NutrientsDto(val kcal: Double, val protein: Double, val carbs: Double, val fat: Double)
