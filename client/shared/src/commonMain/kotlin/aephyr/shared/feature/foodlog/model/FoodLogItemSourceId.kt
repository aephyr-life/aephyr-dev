package aephyr.shared.feature.foodlog.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class  FoodLogItemSourceId(val value: String) : Comparable<FoodLogItemSourceId> {
    override fun compareTo(other: FoodLogItemSourceId): Int =
        value.compareTo(other.value)
}
