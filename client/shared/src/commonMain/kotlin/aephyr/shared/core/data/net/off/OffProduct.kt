package aephyr.shared.core.data.net.off

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffProduct(
    val code: String? = null,
    @SerialName("product_name") val productName: String? = null,
    @SerialName("brands") val brands: String? = null,
    val nutriments: Nutriments? = null
)
