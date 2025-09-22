package aephyr.shared.feature.dashboard

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update // <-- needed
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFacade(
    private val repo: DashboardRepository,
    private val scope: CoroutineScope = MainScope()
) {

    constructor(repo: DashboardRepository) : this(repo, MainScope())

    private val _state = MutableStateFlow(DashboardUi())
    val state: StateFlow<DashboardUi> = _state

    @NativeCoroutines
    fun observeState(): Flow<DashboardUi> = state

    init {
        // Can't call suspend from init; launch a coroutine on Main.
        scope.launch(Dispatchers.Main) {
            refresh()
        }
    }

    /** One-shot load for "today". */
    @NativeCoroutines
    suspend fun refresh() {
        _state.update { it.copy(isLoading = true) }

        // On K/N use Default for background work (or handle dispatchers inside repo).
        val data = withContext(Dispatchers.Default) {
            repo.fetchToday()
        }

        _state.value = DashboardUi(
            isLoading = false,
            hero = data.hero,
            entries = data.entries
        )
    }

    /** Remove an item and refresh list. */
    @NativeCoroutines
    suspend fun remove(id: String) {
        withContext(Dispatchers.Default) {
            repo.remove(id)
        }
        refresh()
    }
}

interface DashboardRepository {
    suspend fun fetchToday(): DashboardData
    suspend fun remove(id: String)
}

data class DashboardData(
    val hero: Hero?,
    val entries: List<FoodItem>
)
