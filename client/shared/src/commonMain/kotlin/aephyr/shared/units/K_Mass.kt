package aephyr.shared.units

import kotlinx.serialization.Serializable

@Serializable
data class K_Mass(val value: Double, val unit: K_MassUnit) {

    operator fun plus(o: K_Mass) : K_Mass =
        K_Mass(value.plus(o.to(unit)), unit)

    fun to(target: K_MassUnit): Double =
        if (unit == target) value else value * unit.gramsPerUnit / target.gramsPerUnit

    override fun toString(): String {
        return "<$value ${unit.symbol}>"
    }

    companion object {
        fun ug(v: Number) = K_Mass(v.toDouble(), K_MassUnit.MICROGRAM)
        fun mg(v: Number) = K_Mass(v.toDouble(), K_MassUnit.MILLIGRAM)
        fun g(v: Number)  = K_Mass(v.toDouble(), K_MassUnit.GRAM)
        fun kg(v: Number) = K_Mass(v.toDouble(), K_MassUnit.KILOGRAM)
    }
}
