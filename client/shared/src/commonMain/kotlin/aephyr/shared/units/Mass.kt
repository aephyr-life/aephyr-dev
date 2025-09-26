package aephyr.shared.units

import kotlinx.serialization.Serializable

@Serializable
data class Mass(val value: Double, val unit: MassUnit) {

    operator fun plus(o: Mass) : Mass =
        Mass(value.plus(o.to(unit)), unit)

    fun to(target: MassUnit): Double =
        if (unit == target) value else value * unit.gramsPerUnit / target.gramsPerUnit

    override fun toString(): String {
        return "<$value ${unit.symbol}>"
    }

    companion object {
        fun ug(v: Number) = Mass(v.toDouble(), MassUnit.MICROGRAM)
        fun mg(v: Number) = Mass(v.toDouble(), MassUnit.MILLIGRAM)
        fun g(v: Number)  = Mass(v.toDouble(), MassUnit.GRAM)
        fun kg(v: Number) = Mass(v.toDouble(), MassUnit.KILOGRAM)
    }
}
