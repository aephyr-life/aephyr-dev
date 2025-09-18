package aephyr.shared.core.data.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodDto(
    val id: String,
    val barcode: String?,
    val name: String,
    val brand: String? = null,
    val per100g: NutrientsDto,
    val verified: Boolean = false
)
