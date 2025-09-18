package aephyr.shared.core.data.net.off

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val count: Int,
    val page: Int,
    @SerialName("page_count") val pageCount: Int,
    @SerialName("page_size") val pageSize: Int,
    val products: List<OffProduct>
)
