package aephyr.shared.feature.foodlog.bridge

import FoodStore
import aephyr.shared.core.bridge.JobCancellable
import aephyr.shared.core.bridge.K_Cancellable
import aephyr.shared.feature.dashboard.FoodItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Swift-friendly facade over K_FoodStore.
 * - Keeps suspend methods as-is (Swift gets completion handlers automatically).
 * - Exposes Flow<StateFlow) as a "watch" API that returns a cancellable.
 *
 * NOTE: Anything used from Swift should be prefixed `K_` per your convention.
 * Ensure your models are named accordingly (e.g., K_FoodItem, K_Mass, ...).
 */
class K_FoodStore(
    private val store: FoodStore,
    private val scope: CoroutineScope = MainScope()
) {
    /** Stream today's items with main-thread emissions for UI. */
    fun watchToday(onEach: (List<FoodItem>) -> Unit): K_Cancellable {
        val job = scope.launch(Dispatchers.Main.immediate) {
            store.today.collect { onEach(it) }
        }
        return JobCancellable(job)
    }

    /** If you also want to expose the Flow version without @NativeCoroutines: */
    fun watchTodayFromFlow(onEach: (List<FoodItem>) -> Unit): K_Cancellable {
        val job = scope.launch(Dispatchers.Main.immediate) {
            store.observeToday()
                .distinctUntilChanged()
                .collect { onEach(it) }
        }
        return JobCancellable(job)
    }
}
