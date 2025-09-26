package aephyr.shared.feature.foodlog.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class FoodName(val value: String) : Comparable<FoodName> {
    override fun compareTo(other: FoodName): Int = value.compareTo(other.value)
    override fun toString() = value
}
