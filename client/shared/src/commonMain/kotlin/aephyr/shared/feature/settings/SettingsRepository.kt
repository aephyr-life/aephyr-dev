package aephyr.shared.feature.settings

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class EnergyUnitPref { KCAL, KJ }

class SettingsRepository(private val settings: Settings) {
    companion object { const val ENERGY_KEY = "energy_unit" } // <- same key as Settings.bundle

    private val _energy = MutableStateFlow(load())
    @NativeCoroutines
    val energy: StateFlow<EnergyUnitPref> = _energy.asStateFlow()

    val currentEnergy: EnergyUnitPref get() = _energy.value

    fun setEnergy(pref: EnergyUnitPref) {
        settings.putString(ENERGY_KEY, if (pref == EnergyUnitPref.KJ) "kJ" else "kcal")
        _energy.value = pref
    }

    /** Re-read from platform settings (call when app becomes active on iOS). */
    fun refreshFromSystem() {
        _energy.value = load()
    }

    private fun load(): EnergyUnitPref =
        when (settings.getString(ENERGY_KEY, "kcal")) {
            "kJ" -> EnergyUnitPref.KJ
            else -> EnergyUnitPref.KCAL
        }
}
