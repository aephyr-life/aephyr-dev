package aephyr.shared.units

import kotlinx.serialization.Serializable

@Serializable
data class Energy(val value: Double, val unit: EnergyUnit) {

    operator fun plus(o: Energy) : Energy =
        Energy(value.plus(o.to(unit)), unit)

    operator fun div(o: Energy) : Double =
        value / o.to(unit)

    fun to(target: EnergyUnit): Double =
        if (unit == target) value
        else value * unit.kJPerUnit / target.kJPerUnit

    override fun toString(): String {
        return "<$value ${unit.symbol}>"
    }

    companion object {
        fun kcal(v: Number) = Energy(v.toDouble(), EnergyUnit.KILOCALORIE)
        fun kJ(v: Number)   = Energy(v.toDouble(), EnergyUnit.KILOJOULE)
    }
}
