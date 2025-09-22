package aephyr.shared.feature.foodlog.model

import aephyr.shared.units.K_Energy

data class K_NewLogInput(
    val consumedAt: K_DietMoment,
    val name: K_FoodName,
    val portion: K_Quantity?,
    val energy: K_Energy?,
    val macros: K_Macros?
)
