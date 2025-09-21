package aephyr.shared.feature.dashboard

/** UI snapshot for the dashboard screen. */
data class DashboardUi(
    val isLoading: Boolean = true,
    val hero: Hero? = null,
    val entries: List<FoodItem> = emptyList()
)

// You can replace these with your real types or import them directly:
data class Hero(val title: String, val subtitle: String? = null)
data class FoodItem(val id: String, val name: String, val grams: Int, val kcal: Int)
