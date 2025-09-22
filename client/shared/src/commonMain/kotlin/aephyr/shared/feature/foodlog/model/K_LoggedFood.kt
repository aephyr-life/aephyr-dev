package aephyr.shared.feature.foodlog.model

import aephyr.shared.units.K_Energy
import aephyr.shared.units.K_Mass
import kotlinx.datetime.Instant

data class K_LoggedFood (
    val id: K_FoodLogId,
    val consumedAt: K_DietMoment,
    val name: K_FoodName,
    val portion: K_Mass? = null,
    val energy: K_Energy? = null,
    val macros: K_Macros? = null,
    val notices: List<K_Notice> = emptyList(),
    val loggedAt: Instant
) : Comparable<K_LoggedFood> {

    fun energyBestEffort(): K_Energy? = energy ?: macros?.totalEnergy

    override fun compareTo(other: K_LoggedFood): Int =
        compareValuesBy(this, other,
            { it.consumedAt },
            { it.loggedAt },
            { it.name },
            { it.id }
        )
}
