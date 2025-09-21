package aephyr.shared.feature.logfood.model

data class K_NewLogInput(
    val consumedAt: K_DietMoment,
    val name: K_FoodName,
    val portion: K_Quantity?,
    val energy: K_KiloJoule?,
    val macros: K_Macros?
)
