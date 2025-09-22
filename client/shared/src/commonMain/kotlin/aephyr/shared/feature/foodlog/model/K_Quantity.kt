package aephyr.shared.feature.foodlog.model

import aephyr.shared.units.K_Mass

sealed interface K_Quantity {
    data class Mass(val value: K_Mass) : K_Quantity
}
