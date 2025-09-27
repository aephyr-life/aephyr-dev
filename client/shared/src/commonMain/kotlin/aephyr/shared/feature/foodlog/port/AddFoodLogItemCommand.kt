package aephyr.shared.feature.foodlog.port

import aephyr.shared.feature.foodlog.model.DietMoment
import aephyr.shared.feature.foodlog.model.FoodLogItemSourceId
import aephyr.shared.feature.foodlog.model.FoodName
import aephyr.shared.feature.foodlog.model.Macros
import aephyr.shared.units.Energy
import aephyr.shared.units.Mass

data class AddFoodLogItemCommand(
    val sourceId: FoodLogItemSourceId? = null,
    val name: FoodName,
    val mass: Mass? = null,
    val energy: Energy? = null,
    val macros: Macros? = null,
    val consumedAt: DietMoment
)
