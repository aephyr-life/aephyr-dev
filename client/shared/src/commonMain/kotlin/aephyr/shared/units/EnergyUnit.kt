package aephyr.shared.units

import kotlinx.serialization.Serializable

@Serializable
enum class EnergyUnit(val kJPerUnit: Double, val symbol: String) {
    KILOCALORIE(4.184, "kcal"),
    KILOJOULE(1.0, "kJ");
}
