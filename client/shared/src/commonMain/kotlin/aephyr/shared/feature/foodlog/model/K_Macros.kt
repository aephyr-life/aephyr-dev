package aephyr.shared.feature.foodlog.model

import aephyr.shared.units.K_Energy
import aephyr.shared.units.K_EnergyUnit
import aephyr.shared.units.K_Mass
import aephyr.shared.units.K_MassUnit
import kotlinx.serialization.Serializable

@Serializable
data class K_Macros(
    val protein: K_Mass,
    val fat: K_Mass,
    val carb: K_Mass
) {

    private fun toEnergy(f: Double, n: K_Mass) : K_Energy = K_Energy(
        f * n.to(K_MassUnit.GRAM),
        K_EnergyUnit.KILOCALORIE
    )

    val proteinEnergy = toEnergy(4.0,protein)
    val fatEnergy     = toEnergy(9.0,fat)
    val carbEnergy    = toEnergy(4.0,carb)
    val totalEnergy   = proteinEnergy + fatEnergy + carbEnergy

    operator fun plus(o: K_Macros) : K_Macros =
        K_Macros(
            protein.plus(o.protein),
            fat.plus(o.fat),
            carb.plus(o.carb),
        )

    companion object {
        val ZERO = K_Macros(
            K_Mass(0.0, K_MassUnit.GRAM),
            K_Mass(0.0, K_MassUnit.GRAM),
            K_Mass(0.0, K_MassUnit.GRAM)
        )
    }
}
