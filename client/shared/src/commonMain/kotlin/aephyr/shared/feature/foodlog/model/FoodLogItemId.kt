package aephyr.shared.feature.foodlog.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class FoodLogItemId(val value: String) : Comparable<FoodLogItemId> {
    override fun compareTo(other: FoodLogItemId): Int = value.compareTo(other.value)
}
