package aephyr.shared.units

import kotlinx.serialization.Serializable

@Serializable
data class K_Energy(val value: Double, val unit: K_EnergyUnit) {

    operator fun plus(o: K_Energy) : K_Energy =
        K_Energy(value.plus(o.to(unit)), unit)

    operator fun div(o: K_Energy) : Double =
        value / o.to(unit)

    fun to(target: K_EnergyUnit): Double =
        if (unit == target) value
        else value * unit.kJPerUnit / target.kJPerUnit

    override fun toString(): String {
        return "<$value ${unit.symbol}>"
    }

    companion object {
        fun kcal(v: Number) = K_Energy(v.toDouble(), K_EnergyUnit.KILOCALORIE)
        fun kJ(v: Number)   = K_Energy(v.toDouble(), K_EnergyUnit.KILOJOULE)
    }
}
