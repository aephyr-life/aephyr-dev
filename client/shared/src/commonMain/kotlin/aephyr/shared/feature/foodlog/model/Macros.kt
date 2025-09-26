package aephyr.shared.feature.foodlog.model

import aephyr.shared.units.Energy
import aephyr.shared.units.EnergyUnit
import aephyr.shared.units.Mass
import aephyr.shared.units.MassUnit
import kotlinx.serialization.Serializable

@Serializable
data class Macros(
    val protein: Mass,
    val fat: Mass,
    val carb: Mass
) {

    private fun toEnergy(f: Double, n: Mass) : Energy = Energy(
        f * n.to(MassUnit.GRAM),
        EnergyUnit.KILOCALORIE
    )

    val proteinEnergy = toEnergy(4.0,protein)
    val fatEnergy     = toEnergy(9.0,fat)
    val carbEnergy    = toEnergy(4.0,carb)
    val totalEnergy   = proteinEnergy + fatEnergy + carbEnergy

    operator fun plus(o: Macros) : Macros =
        Macros(
            protein.plus(o.protein),
            fat.plus(o.fat),
            carb.plus(o.carb),
        )

    companion object {
        val ZERO = Macros(
            Mass(0.0, MassUnit.GRAM),
            Mass(0.0, MassUnit.GRAM),
            Mass(0.0, MassUnit.GRAM)
        )
    }
}
