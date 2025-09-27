package aephyr.shared.feature.foodlog.model

import aephyr.shared.units.Energy
import aephyr.shared.units.Mass
import kotlin.time.Instant

@kotlin.time.ExperimentalTime
data class FoodLogItem (
    val id: FoodLogItemId,
    val consumedAt: DietMoment,
    val name: FoodName,
    val mass: Mass? = null,
    val energy: Energy? = null,
    val macros: Macros? = null,
    val notices: List<Notice> = emptyList(),
    val loggedAt: Instant
) : Comparable<FoodLogItem> {

    fun energyBestEffort(): Energy? = energy ?: macros?.totalEnergy

    override fun compareTo(other: FoodLogItem): Int =
        compareValuesBy(this, other,
            { it.consumedAt },
            { it.loggedAt },
            { it.name },
            { it.id }
        )
}
