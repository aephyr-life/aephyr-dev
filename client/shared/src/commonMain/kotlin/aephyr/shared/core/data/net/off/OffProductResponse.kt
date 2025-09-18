package aephyr.shared.core.data.net.off

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffProductResponse(
    val code: String,
    val status: Int? = null,
    @SerialName("status_verbose") val statusVerbose: String? = null,
    val product: OffProduct? = null
)
