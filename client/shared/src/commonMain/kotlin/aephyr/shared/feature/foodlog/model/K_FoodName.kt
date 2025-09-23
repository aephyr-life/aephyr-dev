package aephyr.shared.feature.foodlog.model

import kotlinx.serialization.Serializable

@Serializable
value class K_FoodName(val value: String) : Comparable<K_FoodName> {
    override fun compareTo(other: K_FoodName): Int = value.compareTo(other.value)
    override fun toString() = value
}
