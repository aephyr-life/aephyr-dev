package aephyr.shared.feature.foodlog.model

sealed interface Quantity {
    data class Mass(val value: aephyr.shared.units.Mass) : Quantity
}
