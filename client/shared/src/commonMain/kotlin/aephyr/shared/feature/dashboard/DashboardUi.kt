package aephyr.shared.feature.dashboard

data class DashboardUi(
    val isLoading: Boolean = true,
    val hero: Hero? = null,
    val entries: List<FoodItem> = emptyList()
)

// Stub your domain types or import real ones
data class Hero(val title: String, val subtitle: String? = null)
data class FoodItem(val id: String, val name: String, val grams: Int, val kcal: Int)
