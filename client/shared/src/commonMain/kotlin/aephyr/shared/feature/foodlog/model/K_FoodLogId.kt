package aephyr.shared.feature.foodlog.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class K_FoodLogId(val value: String) : Comparable<K_FoodLogId> {
    override fun compareTo(other: K_FoodLogId): Int = value.compareTo(other.value)
}
