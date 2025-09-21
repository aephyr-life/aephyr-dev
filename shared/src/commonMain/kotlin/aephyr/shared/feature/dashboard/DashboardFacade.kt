package aephyr.shared.feature.dashboard

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Facade owned by KMM. Holds dashboard UI state and exposes suspend functions for Swift.
 */
class DashboardFacade(
    private val repo: DashboardRepository,
    private val scope: CoroutineScope = MainScope()
) {
    private val _state = MutableStateFlow(DashboardUi())
    @NativeCoroutinesState
    val state: StateFlow<DashboardUi> = _state

    init {
        // immediately load on creation
        scope.launch { refresh() }
    }

    /** One-shot load for "today". */
    @NativeCoroutines
    suspend fun refresh() {
        _state.update { it.copy(isLoading = true) }
        val data = withContext(Dispatchers.IO) { repo.fetchToday() }
        _state.value = DashboardUi(
            isLoading = false,
            hero = data.hero,
            entries = data.entries
        )
    }

    /** Remove an item and then refresh the list. */
    @NativeCoroutines
    suspend fun remove(id: String) {
        withContext(Dispatchers.IO) { repo.remove(id) }
        refresh()
    }
}

/** Repository interface your existing code should implement. */
interface DashboardRepository {
    suspend fun fetchToday(): DashboardData
    suspend fun remove(id: String)
}

data class DashboardData(
    val hero: Hero?,
    val entries: List<FoodItem>
)
