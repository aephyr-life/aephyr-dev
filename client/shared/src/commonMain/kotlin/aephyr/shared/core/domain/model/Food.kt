package aephyr.shared.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: String,
    val barcode: String?,
    val name: String,
    val brand: String? = null,
    val per100g: Nutrients,
    val verified: Boolean = false,
)
