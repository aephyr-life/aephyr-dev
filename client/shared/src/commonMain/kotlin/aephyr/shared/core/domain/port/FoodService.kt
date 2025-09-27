package aephyr.shared.core.domain.port

import aephyr.shared.core.domain.model.Food
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines

interface FoodService {
    @NativeCoroutines
    suspend fun byBarcode(code: String): Food?
    @NativeCoroutines
    suspend fun search(query: String): List<Food>
}
