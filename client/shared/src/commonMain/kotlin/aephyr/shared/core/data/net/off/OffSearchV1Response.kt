package aephyr.shared.core.data.net.off

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffSearchV1Response(
    val count: Int,
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
    val products: List<OffProductV1>
)

@Serializable
data class OffProductV1(
    val code: String? = null,
    @SerialName("product_name") val productName: String? = null,
    val brands: String? = null,
    val nutriments: NutrimentsV1? = null
)

@Serializable
data class NutrimentsV1(
    @SerialName("energy-kcal_100g") val kcal: Double? = null,
    @SerialName("proteins_100g") val protein100g: Double? = null,
    @SerialName("carbohydrates_100g") val carbs100g: Double? = null,
    @SerialName("fat_100g") val fat100g: Double? = null
)
