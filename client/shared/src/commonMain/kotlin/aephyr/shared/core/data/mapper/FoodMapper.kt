package aephyr.shared.core.data.mapper

import aephyr.shared.core.data.net.dto.*
import aephyr.shared.core.domain.model.*

fun FoodDto.toDomain() = Food(
    id = id,
    barcode = barcode,
    name = name,
    brand = brand,
    per100g = per100g.toDomain(),
    verified = verified
)

private fun NutrientsDto.toDomain() =
    Nutrients(kcal, protein, carbs, fat)
