import aephyr.shared.feature.dashboard.FoodItem
import aephyr.shared.units.K_Energy
import aephyr.shared.feature.foodlog.model.K_Macros
import aephyr.shared.units.K_Mass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FoodStore {
    val today: StateFlow<List<FoodItem>>

    fun observeToday(): Flow<List<FoodItem>> = today

    suspend fun add(name: String, mass: K_Mass?, energy: K_Energy?, macros: K_Macros? = null): FoodItem

    suspend fun remove(id: String)
}
