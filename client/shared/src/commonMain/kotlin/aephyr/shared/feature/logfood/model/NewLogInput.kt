package aephyr.shared.feature.logfood.model

data class NewLogInput(
    val consumedAt: DietMoment,
    val name: FoodName,
    val portion: Quantity?,
    val energy: KiloJoule?,
    val macros: Macros?
)
