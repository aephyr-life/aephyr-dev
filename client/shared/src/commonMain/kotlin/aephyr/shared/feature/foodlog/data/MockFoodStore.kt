package aephyr.shared.feature.foodlog.data

import FoodStore
import aephyr.shared.feature.dashboard.FoodItem
import aephyr.shared.feature.foodlog.model.K_Macros


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import aephyr.shared.units.*
import kotlin.random.Random

class MockFoodStore(seed: List<FoodItem> = emptyList()) : FoodStore {
    private val _today = MutableStateFlow(seed)
    override val today: StateFlow<List<FoodItem>> = _today

    override suspend fun add(name: String, mass: K_Mass?, energy: K_Energy?, macros: K_Macros?): FoodItem {
        val id = Random.nextLong().toString()
        val item = FoodItem(id, name, mass, energy, macros)
        _today.update { it + item }
        return item
    }

    override suspend fun remove(id: String) {
        _today.update { it.filterNot { it.id == id } }
    }

    companion object {
        fun withSeeds() = MockFoodStore(
            listOf(
                FoodItem("1", "Greek Yogurt", mass = K_Mass.g(200), energy = K_Energy.kcal(120)),
                FoodItem("2", "Banana",       mass = K_Mass.g(120), energy = K_Energy.kcal(105))
            )
        )
    }
}
