package aephyr.shared.feature.dashboard

import aephyr.shared.units.K_Energy
import aephyr.shared.feature.foodlog.model.K_Macros
import aephyr.shared.units.K_Mass

data class DashboardUi(
    val isLoading: Boolean = true,
    val hero: Hero? = null,
    val entries: List<FoodItem> = emptyList()
)

// Stub your domain types or import real ones
data class Hero(val title: String, val subtitle: String? = null)
@kotlinx.serialization.Serializable
data class FoodItem(
    val id: String,
    val name: String,
    val mass: K_Mass? = null,
    val energy: K_Energy? = null,
    val macros: K_Macros? = null
)
