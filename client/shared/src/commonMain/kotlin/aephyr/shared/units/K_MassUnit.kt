package aephyr.shared.units

import kotlinx.serialization.Serializable

@Serializable
enum class K_MassUnit(val gramsPerUnit: Double, val symbol: String) {
    MICROGRAM(0.0000_001, "ug"),
    MILLIGRAM(0.001, "mg"),
    GRAM(1.0, "g"),
    KILOGRAM(1000.0, "kg");
}
